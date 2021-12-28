(ns aleph-tcp-sample.core
  (:require [aleph.tcp :as tcp]
            [manifold.deferred :as d]
            [manifold.stream :as s]
            [clojure.string :as str]
            [byte-streams :as bs]))

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
  (d/loop [n 100] ; MaxKeepAliveRequests
    (-> (d/timeout! (s/take! s) 5000 :timeout) ;KeepAliveTimeout
        (d/chain (fn [x]
                   (if (= :timeout x)
                     (s/close! s)
                     (do (->> x
                              (bs/to-string)
                              (response-echo)
                              (bs/to-byte-buffer)
                              (s/put! s))
                         (if (pos? n)
                           (d/recur (dec n))
                           (s/close! s)))))))))

(defn go []
  (reset! echo-server (tcp/start-server echo-handler {:port 10001})))

(defn halt []
  (when @echo-server
    (.close @echo-server)))

(defn reset []
  (halt)
  (go))
