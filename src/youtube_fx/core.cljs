(ns youtube-fx.core
  (:require [re-frame.core :as re-frame]
            [camel-snake-kebab.core :as convert]
            [youtube-fx.reg-fx :as fx]
            [youtube-fx.global :as global]))

(defn event->callback [event-vec]
  #(re-frame/dispatch (vec (concat event-vec %&))))

(defn events->callbacks [events-map]
  (into {} (for [[k v] events-map]
             [(convert/->camelCase k)
              (event->callback v)])))

(defn ->youtube
  "Thin wrapper over the Player constructor, casts options map from idiomatic cljs to js
  Example:
   {:height \"350\"
    :width \"480\"
    :events {:on-ready [:player-ready]
             :on-state-change [:player-state-change]}}
   becomes
   #js {:height \"350\"
        :width \"480\"
        :events #js {:onReady #(dispatch [:player-ready %])
                     :onStateChange #(dispatch [:player-state-change %])}}"
  [element-id options]
  (let [Player (.-Player js/YT)]
    (Player. element-id
             (-> options
                 (update :events events->callbacks)
                 clj->js))))

(re-frame/reg-fx
 :youtube/initialize-player
 ;; TODO some validation on options
 (fn [[player-id options :as args]]
   (assert (keyword? player-id))
   (->> options
        (->youtube (name player-id))
        (global/new-player player-id))))

(doseq [method fx/known-methods]
  (fx/youtube-method-reg-fx! method))
