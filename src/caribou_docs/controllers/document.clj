(ns caribou-docs.controllers.document
  (:use caribou.app.controller)
  (:require [caribou.model :as model]
            [caribou.util :as util]
            [caribou.app.pages :as pages]
            [caribou.admin.helpers :as admin-helpers]
            [markdown.core :as markdown]))


(defn format-content
  [model content]
  (let [translators {:markdown markdown/md-to-html-string}
        fun (fn [i k v]
              (let [preserved (assoc i k v)
                    fmt (-> model :fields k :row :format)
                    translator (if (empty? fmt)
                                 identity
                                 (get translators (keyword fmt) identity))
                    translated (if (empty? fmt)
                                 preserved
                                 (assoc preserved (keyword (str (name k) "-" fmt)) (translator v)))]
                translated))]
    (reduce-kv fun {} content)))

(defn linkify
  [request match]
  (let [long-form (re-matches #"(.+)\s*\|\s*(.+)" (second match))
        term (if-not (nil? long-form)
               (second long-form)
               (second match))
        text (if-not (nil? long-form)
               (nth long-form 2)
               term)
        slug (util/url-slugify term)
        url (pages/route-for :view-document (assoc (-> request :params) :slug slug))]
    (str "<a href=\"" url "\">" text "</a>")))

(defn wikify
  ([request text]
     (wikify request text {:re #"\{\{(.+?)\}\}"}))
  ([request text opts]
     (clojure.string/replace text (:re opts) (partial linkify request))))

(defn index
  [request]
  (let [pager (admin-helpers/add-pagination (model/gather :document {:order {:created-at :asc}})
                                            {:page-size 25
                                             :current-page (-> request :params :page)})]
    (render (assoc request :pager pager))))

(defn view
  [request]
  (let [slug-or-id (-> request :params :slug)
        id (when (re-matches #"^\d+$" slug-or-id)
             slug-or-id)
        document (if id
                   (model/pick :document {:where {:id id}})
                   (model/pick :document {:where {:slug slug-or-id}}))]
    (if (not (nil? document))
      (render (assoc request :document (format-content (model/models :document) document)
                     :wikify (partial wikify request)))
      (if (-> request :session :admin :user)
        (redirect (pages/route-for :new-document {:slug slug-or-id}))
        {:status 404 :body "No such page"}))))

(defn edit
  [request]
  (if-not (-> request :session :admin :user)
    {:status 400 :body "Naughty!"}
    (render (assoc request
              :document (model/pick :document {:where {:id (-> request :params :id)}})))))

(defn new
  [request]
  (let [document {:slug (-> request :params :slug)}]
    (render (assoc request :document document))))

(defn save
  [request]
  (let [params (-> request :params)
        document (select-keys params [:id :title :slug :body])
        ;; uncomment these to get versioning "working"
        ;;picked (when-not (empty? (:id document))
        ;;         (model/pick :document {:where {:id (:id document)}}))
        authored (assoc document :author-id (-> request :session :admin :user :id))
        saved (model/create :document authored)
        ;;deparented (assoc authored :id nil :parent-id nil)
        ;;saved (model/create :document deparented)
        ;;obviated (when-not (nil? picked)
        ;;           (model/create :document (assoc picked :parent-id (:id saved))))
        ]
    (redirect (pages/route-for :view-document {:slug (:slug saved)}))))

(defn delete
  [request]
  (let [id (-> request :params :id)
        document (model/pick :document {:where {:id id}})]
    (if (and document (-> request :session :admin :user))
      (do (model/destroy :document id) document)
      {:status 400 :body "Naughty naughty"})))
