(ns msoto.main
  (:require [org.httpkit.server :refer [run-server]]
            [msoto.endpoints :refer [handler]])
  (:gen-class))

(defonce server-atom (atom nil))

(defn start-server! []
  (if (deref server-atom)
    "Server already started"
    (reset! server-atom (run-server (fn [request]
                                      (handler request))
                                    {:port 4711}))))

(defn stop-server! []
  (if-let [stop-server-fn (deref server-atom)]
    (do (stop-server-fn :timeout 100)
        (reset! server-atom nil))
    "Server not running"))


;(defn stop-server! []
;  (when (deref server-atom)
;    (let [stop-server-fn (deref server-atom)]
;      (stop-server-fn :timeout 100))
;    (reset! server-atom nil)))

; quick definition docs

(defn -main [& _]
  (println "hello world"))


(comment
  (start-server!)
  (stop-server!)
  )
