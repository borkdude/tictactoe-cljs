(ns tictactoe.app
  (:require [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]))

(enable-console-print!)

;;; State free game logic ;;;

(def empty-board [[\- \- \-]
                  [\- \- \-]
                  [\- \- \-]])

(def init-state {:board empty-board :player \X})

(defn get-board-cell
  ([board row col]
     (get-in board [row col])))

(defn get-player [app-state]
  (-> app-state :game-state :player))

(defn other-player [player]
  (if (= player \X) \O \X))

(defn winner-in-rows? [board player]
  (boolean (some (fn [row] (every? (fn [c] (= c player)) row)) board)))

(defn transposed-board [board]
  (vec (apply map vector board)))

(defn winner-in-cols? [board player]
  (winner-in-rows? (transposed-board board) player))

(defn winner-in-diagonals? [board player]
  (let [diag-coords [[[0 0] [1 1] [2 2]]
                     [[0 2] [1 1] [2 0]]]]
    (boolean (some (fn [coords]
                     (every? (fn [coord]
                               (= player (apply get-board-cell board coord)))
                             coords))
                   diag-coords))))

(defn winner?
  "checks if there is a winner. when called with no args, checks for player X and player O.
returns the character for the winning player, nil if there is no winner"
  ([board]
     (boolean (or (winner? board \X)
                  (winner? board \O))))
  ([board player]
     (if (or (winner-in-rows? board player)
             (winner-in-cols? board player)
             (winner-in-diagonals? board player))
       player)))

(defn full-board?
  [board]
  (let [all-cells (apply concat board)]
    (not-any? #(= % \-) all-cells)))

(defn new-state [row col old-state]
  (if (and (= (get-board-cell (:board old-state) row col) \-)
           (not (winner? (:board old-state))))
    {:board (assoc-in (:board old-state) [row col] (:player old-state))
     :player (other-player (:player old-state))}
    old-state))

;;; End game logic

;;; Om logic
(defn tictactoe-row
  [cursor x]
  (apply dom/tr nil
         (for [y (range 3)]
           (dom/td #js {:onClick
                        (fn [e]
                          (om/update! cursor
                                      (new-state x y
                                                 (om/value cursor))))}
                   (dom/div nil
                            (get-board-cell (:board (om/value cursor))
                                            x
                                            y))))))

(defn tictactoe-board
  [cursor owner]
  (om/component
   (apply dom/table nil
          (for [x (range 3)]
            (tictactoe-row cursor x)))))

;; useful for debugging
(defn app-literal [data owner]
  (om/component
   (dom/pre nil
            (pr-str data))))

(defn winner-status [data owner]
  (om/component
   (dom/h1 nil
           (if-let [winner (winner? (:board (om/value data)))]
             (str "The winner is " (other-player (:player (om/value data))))
             (if (full-board? (:board (om/value data)))
               "It's a draw"
               (str "Your turn, player " (:player (om/value data))))))))

(defn reset-game! [cursor]
  (om/update! cursor :game-state init-state))

(defn app-view [data owner]
  (reify
    om/IDidMount
    (did-mount [_]
      (reset-game! data))
    om/IRender
    (render [_]
      (dom/div #js {:id "wrapper"}
               (om/build winner-status (:game-state data))
               #_(om/build app-literal (:game-state data))
               (om/build tictactoe-board (:game-state data))))))

(om/root
 app-view
 {}
 {:target (.getElementById js/document "app")})
