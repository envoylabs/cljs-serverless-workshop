(ns cljs-serverless-workshop.core
  (:require [cljs.nodejs :as nodejs]
            [testdouble.cljs.csv :as csv]
            [httpurr.client.node :as http]
            [httpurr.status :as s]
            [promesa.core :as p]
            [cljs-lambda.macros :refer-macros [deflambda defgateway]]))

(nodejs/enable-util-print!)

(def csv-location "https://gist.githubusercontent.com/the-frey/e0184b9237f162076b85b1101ac4851b/raw/1dbaa84cb0d0d24f542f723beb20f317d53bf554/eff_wordlist.csv")

(defn clj->json [clj-map]
  "Takes a clj map and returns a JSON map"
  (let [json-map (->> clj-map
                      clj->js
                      (.stringify js/JSON))]
    json-map))

;; env utils
(defn get-env
  "Gets the env vars from the AWS
   container running the lambda"
  []
  "Returns current env vars as a Clojure map."
  (-> (.-env js/process)
      js->clj)) 

(defn get-from-env
  "A utility function so that we can e.g.
   get SOME_CREDENTIAL from env. Takes a string"
  [env-var]
  (let [env-value (-> (get-env)
                      (aget env-var))]
    env-value))

(defn process-response
  "Similar to the fn in the docs, but
   without destructuring the body
   as it won't be a map or JSON"
  [response]
  (condp = (:status response)
    s/ok           (p/resolved (-> (:body response)
                                   csv/read-csv))
    s/not-found    (p/rejected :not-found)
    s/unauthorized (p/rejected :unauthorized)))

;; http utils
(defn get-csv-file
  "Implemented as an http request. A CSV can be
   found at the location def'd above."
  []
  (p/then (http/get csv-location)
          process-response))

(comment (p/then (get-csv-file)
                 (fn [res] (print res))))

(defgateway hello [event ctx]
  (p/chain (get-csv-file)
           (fn [res] 
             {:statusCode 200
              :headers {"Content-Type" "application/json"}
              :body (clj->json res)}))) 

(set! (.-exports js/module) #js
      {:hello hello})
