(ns barcamp.core
  (:require [compojure.core :refer [defroutes GET POST HEAD ANY]]
            [compojure.route :as route]
            [ring.adapter.jetty :refer [run-jetty]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.keyword-params :refer [wrap-keyword-params]]
            [ring.middleware.cookies :refer [wrap-cookies]]
            [clojure.pprint :refer [pprint]]))

(defn handler [req]
  (str "<pre>" (with-out-str (pprint req))))

#_(defn wrap-foo [handler]
  (fn [req]
    (handler (assoc req :foo "wheee!"))))

#_(defroutes routes
  (GET "/foo" req (handler req))
  (POST "/bar" req (handler2 req)))

#_(defroutes routes
  (GET "/foo/:bar" [bar] (handler bar)))

(defroutes routes

  (route/resources "/")

  (ANY "*" req handler)
  
  (HEAD "*" _ {:status 404})
  (ANY "*" _ {:status 404 :body "not found"}))

(def app (-> #'routes
           ;;wrap-foo
           ;;wrap-keyword-params
           ;;wrap-params
           ;;wrap-cookies
           ))

(defn -main []
  (run-jetty #'app {:port (try
                            (Integer/parseInt (System/getenv "PORT"))
                            (catch Exception _ 8080))
                    :join? false}))