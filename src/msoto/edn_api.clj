(ns msoto.edn-api
  (:require [msoto.core :as core])
  (:import (java.time LocalDateTime)))

(defonce db-atom (atom {:games {}}))

(defn now []
  (.toString (LocalDateTime/now)))

(defn handle-event [{game-name   :value1
                     next-player :value2
                     game-turn   :value3}]
  (swap! db-atom core/merge-event {:game-name   game-name
                                   :next-player next-player
                                   :game-turn   game-turn
                                   :timestamp   (now)}))

(defn get-past-day-events
  [{game-name :game-name}]
  (core/get-past-day-events (deref db-atom) game-name (now)))


(comment
  (spit "data/db.edn" (str {:foo "123"}))
  (slurp "data/db.edn")

  (reset! db-atom {:games {}})


  {:games {"Martins Spel" {:events [{:timestamp   "2020/10/10"
                                     :game-turn   1
                                     :next-player "Martin"}]}
           "Tomas spel"   {}}}
  )

