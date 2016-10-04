(ns youtube-fx.reg-fx
  (:require [camel-snake-kebab.core :as convert]
            [re-frame.core :refer [reg-fx]]
            [youtube-fx.global :as global]))

(defn camel-keys [map]
  (into { }
        (for [[k v] map]
          [(convert/->camelCase k) v])))

(defn player-call
  [method [player & args]]
  (->> args
       (map (fn [x] (if (map? x) (camel-keys x) x)))
       (clj->js)
       (.apply (aget player method) player)))

(defn resolve-args
  [[player-id & rest :as args]]
  (if-let [player (global/get-player player-id)]
    (apply vector player rest)
    (throw
     (if (keyword? player-id)
       (ex-info "Couldn't find Player" {:id player-id})
       (ex-info "Need to specify a Player with a key" {:args args})))))
 
(defn youtube-method-reg-fx!
  [method]
  (reg-fx
   (keyword "youtube" (convert/->kebab-case method))
   (fn [args]
     (->> (if (vector? args) args [args])
          (resolve-args)
          (player-call method)))))

(def ^:const known-methods
  ["addCueRange"
   "addInfoCardXml"
   "canPlayType"
   "cancelPlayback"
   "clearVideo"
   "cuePlaylist"
   "cueVideoById"
   "cueVideoByPlayerVars"
   "cueVideoByUrl"
   "forceFrescaUpdate"
   "getApiInterface"
   "getAudioTrack"
   "getAvailableAudioTracks"
   "getAvailablePlaybackRates"
   "getAvailableQualityLevels"
   "getCurrentPlaylistSequence"
   "getCurrentTime"
   "getDebugText"
   "getDuration"
   "getHousebrandProperties"
   "getMaxPlaybackQuality"
   "getPlaybackQuality"
   "getPlaybackRate"
   "getPlayerState"
   "getPlaylist"
   "getPlaylistId"
   "getPlaylistIndex"
   "getPresentingPlayerType"
   "getProgressState"
   "getStoryboardFormat"
   "getSubtitlesUserSettings"
   "getVideoBytesLoaded"
   "getVideoBytesTotal"
   "getVideoContentRect"
   "getVideoData"
   "getVideoLoadedFraction"
   "getVideoStartBytes"
   "getVideoStats"
   "getVideoUrl"
   "getVisibilityState"
   "getVolume"
   "handleGlobalKeyDown"
   "hideControls"
   "hideVideoInfo"
   "isFastLoad"
   "isMuted"
   "isPeggedToLive"
   "loadModule"
   "loadPlaylist"
   "loadVideoById"
   "loadVideoByPlayerVars"
   "loadVideoByUrl"
   "mute"
   "nextVideo"
   "pauseVideo"
   "playVideo"
   "playVideoAt"
   "preloadVideoByPlayerVars"
   "previousVideo"
   "removeCueRange"
   "removeEventListener"
   "resetSubtitlesUserSettings"
   "seekBy"
   "seekTo"
   "sendVideoStatsEngageEvent"
   "setAudioTrack"
   "setAutonav"
   "setAutonavState"
   "setCardsVisible"
   "setFauxFullscreen"
   "setLoop"
   "setMinimized"
   "setOption"
   "setPlaybackQuality"
   "setPlaybackQualityRange"
   "setPlaybackRate"
   "setSafetyMode"
   "setShuffle"
   "setSizeStyle"
   "setVolume"
   "shouldSendVisibilityState"
   "showControls"
   "showVideoInfo"
   "stopVideo"
   "unMute"
   "unloadModule"
   "updateLastActiveTime"
   "updatePlaylist"
   "updateSubtitlesUserSettings"
   "updateVideoData"])
