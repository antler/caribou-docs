(ns caribou-docs.controllers.home
  (:use caribou.app.controller)
  (:require [caribou.model :as model]))

(defn home
  [request]
  (render (assoc request :verbed "Started")))
