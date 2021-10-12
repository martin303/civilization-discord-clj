(ns msoto.core
  (:require [clojure.test :refer [deftest is]])
  (:import (java.time LocalDateTime)))

; conjoin-it.se

(defn get-game
  {:test (fn []
           (let [db {:games {"game1" {:events (list {:timestamp   "123"
                                                     :game-turn   "2"
                                                     :next-player "player1"})}}}]
             (is (get-game db "game1"))
             (is (not (get-game db "game2")))))}
  [db game-name]
  (get-in db [:games game-name]))

(defn merge-event
  {:test (fn []
           (is (= {:games {"game1" {:events (list {:timestamp   "123"
                                                   :game-turn   "2"
                                                   :next-player "player1"})}}}
                  (merge-event {:games {}}
                               {:game-name   "game1"
                                :next-player "player1"
                                :game-turn   "2"
                                :timestamp   "123"})))
           (is (= {:games {"game1" {:events (list {:timestamp   "124"
                                                   :game-turn   "3"
                                                   :next-player "player2"}
                                                  {:timestamp   "123"
                                                   :game-turn   "2"
                                                   :next-player "player1"})}}}
                  (merge-event {:games {"game1" {:events (list {:timestamp   "123"
                                                                :game-turn   "2"
                                                                :next-player "player1"})}}}
                               {:game-name   "game1"
                                :next-player "player2"
                                :game-turn   "3"
                                :timestamp   "124"}))))}
  [db {game-name :game-name :as event}]
  {:pre [db game-name]}
  ;(if-let [game (get-game db game-name)]
  (update-in db [:games game-name :events] conj (select-keys event [:timestamp :game-turn :next-player]))
  ;(assoc-in db [:games game-name :events] (list (select-keys event [:timestamp :game-turn :next-player])))
  )
;)

(defn get-past-day-events
  {:test (fn[]
           (is (= 2
                  (count (get-past-day-events {:games {"Game name" {:events (list {:timestamp "2021-10-09T16:30:29.562", :game-turn "2", :next-player "player 2"}
                                                                             {:timestamp "2021-10-09T16:30:06.658", :game-turn "1", :next-player "Next player"})}}}
                                              "Game name"
                                              "2021-10-09T17:30:06.658")))))}
  [db game-name now]
  (let [now-date-time (LocalDateTime/parse now)
        past-day-date-time (.minusDays now-date-time 1)
        game (get-game db game-name)
        events (:events game)]
    (loop [[event & rest-of-events] events
           result []]
      (if-not event
        result
        (let [event-date-time (LocalDateTime/parse (:timestamp event))]
          (if (.isBefore past-day-date-time event-date-time)
            (recur rest-of-events
                   (conj result event))
            result))))))

;(def a (atom {:value 42}))
;
;(swap! a update :value inc)
;
;(deref a)
;
;(swap! a update :value (fn [old-value]
;                         (println "OLD" old-value)
;                         (throw (RuntimeException.))))
;
;(defonce at (atom 0))
;
;(reset! at 0)
;
;(dotimes [_ 10]
;  (future (dotimes [_ 10000]
;            (loop []
;              (let [old-value (deref at)
;                    new-value (inc old-value)
;                    result (compare-and-set! at old-value new-value)]
;                (when-not result
;                  (recur))))
;            (swap! at inc))))






