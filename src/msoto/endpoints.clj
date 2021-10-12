(ns msoto.endpoints
  (:require [msoto.edn-api :as edn-api]
            [clojure.data.json :refer [read-str write-str]]))

(defn handler [request]
  (let [uri (:uri request)
        body (:body request)]
    (cond (= uri "/civ_next_turn_event")
          (let [body-as-edn (read-str (slurp body) :key-fn keyword)]
            {:status  200
             :headers {"Content-Type" "application/json"}
             :body    (write-str (edn-api/handle-event body-as-edn))})

          (= uri "/get_past_day_events")
          (let [body-as-edn (read-str (slurp body) :key-fn keyword)]
            {:status  200
             :headers {"Content-Type" "application/json"}
             :body    (write-str (edn-api/get-past-day-events body-as-edn))})

          :else
          (do
            (clojure.pprint/pprint request)
            {:status  404
             :headers {"Content-Type" "text/html"}
             :body    "Not found"}))))
