(defproject aleph-tcp-sample "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.3"]
                 [manifold "0.2.3"]
                 [aleph "0.4.6"]
                 [org.clj-commons/byte-streams "0.2.10"]]
  :repl-options {:init-ns aleph-tcp-sample.core})
