(ns basic-player.core
    (:require [reagent.core :as reagent]
              [re-frame.core :as re-frame]
              [devtools.core :as devtools]
              [basic-player.handlers]
              [basic-player.subs]
              [basic-player.views :as views]
              [youtube-fx.core]))


(defn dev-setup []
  (when ^boolean js/goog.DEBUG
    (enable-console-print!)
    (println "dev mode")
    (devtools/install!)))

(defn mount-root []
  (reagent/render [views/main-panel]
                  (.getElementById js/document "controls")))

(defn ^:export init []
  (js/setTimeout #(re-frame/dispatch [:initialize-youtube]) 2000)
  (dev-setup)
  (mount-root))
