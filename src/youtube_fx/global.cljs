(ns youtube-fx.global)

(def video-players (atom {}))

(defn new-player
  [id player]
  (swap! video-players assoc id player))

(defn first-player [] (val (first @video-players)))

(defn get-player [id] (get @video-players id))

(defn single-player? [] (= 1 (count @video-players)))
