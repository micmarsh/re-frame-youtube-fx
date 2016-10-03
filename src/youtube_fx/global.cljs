(ns youtube-fx.global)

(def video-players (atom {}))

(defn new-player
  [id player]
  (swap! video-players assoc id player))

(defn get-player [id] (get @video-players id))
