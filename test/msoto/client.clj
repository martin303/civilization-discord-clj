(ns msoto.client
  (:require [clojure.test :refer [deftest is]]
            [org.httpkit.client :refer [request]]
            [clojure.data.json :refer [write-str read-str]]))

(deftest happy
  (is (= 200
         (-> (request {:url     "http://localhost:4711/civ_next_turn_event"
                       :method  :post
                       :body    (write-str {:value1 "Game name"
                                            :value2 "Next player"
                                            :value3 "1"})
                       :timeout 1000})
             (deref)
             (:status)))))

(comment
  (-> (request {:url     "http://localhost:4711/civ_next_turn_event"
                :method  :post
                :body    (write-str {:value1 "Game name"
                                     :value2 "player 2"
                                     :value3 "2"})
                :timeout 1000})
      (deref)
      (:body)
      (read-str :key-fn keyword))


  (-> (request {:url     "http://localhost:4711/get_past_day_events"
                :method  :post
                :body    (write-str {:game-name "Game name"})
                :timeout 1000})
      (deref)
      (:body)
      (read-str :key-fn keyword))

  (def m {0 10
   :a 42
   :b "f"})

  ((clojure.set/map-invert m) 42)

  )
