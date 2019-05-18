(ns cljs-serverless-workshop.core
  (:require [cljs.nodejs :as nodejs]))

(nodejs/enable-util-print!)

(def csv-location "https://gist.githubusercontent.com/the-frey/e0184b9237f162076b85b1101ac4851b/raw/1dbaa84cb0d0d24f542f723beb20f317d53bf554/eff_wordlist.csv")

(defn get-csv-file
  "Implement this as either a file load
   or as an http request. A CSV can be
   found at the location above."
  [])

(defn clj->json [clj-map]
  "Takes a clj map and returns a JSON map"
  (let [json-map (->> clj-map
                      clj->js
                      (.stringify js/JSON))]
    json-map))

(defn hello [event ctx cb]
 (cb nil (clj->js
          {:statusCode 200
           :headers {"Content-Type" "application/json"}
           :body (clj->json
                  {:msg "Hello, serverless!"})})))

(set! (.-exports js/module) #js
      {:hello hello})

