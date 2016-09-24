(ns basic-player.handlers
    (:require [re-frame.core :as re-frame]
              [clojure.string :as str]))

(re-frame/reg-event-fx
 :player-ready
 (fn [_ [_ event]]
   (println "yo ready")
   { }))

(def ^:const player-data
  [:youtube-player
   {:height "350"
    :width "480"
    :events {:on-ready [:player-ready]
             :on-state-change [:player-state-change]}}])

(re-frame/reg-event-fx
 :initialize-youtube
 (constantly {:youtube/initialize-player player-data}))

(defn string->youtube-id [string]
  (-> string
      (str/trim)
      (str/split #"v=")
      (last)
      (str/split #"&")
      (first)))

(re-frame/reg-event-fx
 :load-video
 (fn [_ [_ input]]
   {:youtube/load-video-by-id (string->youtube-id input)}))

(re-frame/reg-event-fx
 :pause-video
 (fn [_ _] {:youtube/pause-video nil}))

(re-frame/reg-event-fx
 :play-video
 (fn [_ _] {:youtube/play-video nil}))

(re-frame/reg-event-fx
 :player-state-change
 (fn [_ [_ event]]
   (println "player changed" event)
   {}))
