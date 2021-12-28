(ns aleph-tcp-sample.core
  (:require [aleph.tcp :as tcp]
            [manifold.deferred :as d]
            [manifold.stream :as s]
            [clojure.string :as str]))

(def echo-server (atom nil))

(defn response-echo [^String x]
  (str/join "\r\n"
            ["HTTP/1.1 200 OK"
             "Server: echo-server"
             (str "Date: " (.format java.time.format.DateTimeFormatter/RFC_1123_DATE_TIME
                                    (java.time.OffsetDateTime/now java.time.ZoneOffset/UTC)))
             "Content-Type: text/plain"
             (str "Content-Length: " (.length x))
             ""
             x]))

(defn echo-handler [s info]
  (s/connect s s))

(defn go []
  (reset! echo-server (tcp/start-server echo-handler {:port 10001})))

(defn halt []
  (when @echo-server
    (.close @echo-server)))

(defn reset []
  (halt)
  (go))
