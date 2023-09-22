# re-frame-youtube-fx

A re-frame library that integrates the YouTube iframe API into re-frame effects, and handles management of global player instances.

## Usage

### Dependency
```clojure
:dependencies [[re-frame-youtube-fx "0.1.1"]]
```

### Initialize Effects
```clojure
(ns app.core
  (:require
    ...
    [youtube-fx.core]   ;; <-- add this
    ...))
```
To register effect handlers

### API Usage

#### Initialization
To get started, make sure your `<script src="https://www.youtube.com/iframe_api"></script>` script is loaded, then use the `:youtube/initialize-player` effect:
```clojure
(re-frame/reg-event-fx
 :initialize-youtube ;; Or whatever your event handler is
 (fn [_ _]
   {:youtube/initialize-player [:youtube-player
                                {:height "350"
                                 :width "480"
                                 :events {:on-ready [:player-ready]
                                          :on-state-change [:player-state-change]}}]
```
The first argument to the vector (in this case `:youtube-player`) must be a keyword indicating the id of the html element to attach the player to, akin to the [first argument of the `Player` constructor](https://developers.google.com/youtube/iframe_api_reference#Loading_a_Video_Player).

The second map of options is also equivalent to the [options argument of the `Player` constructor](https://developers.google.com/youtube/iframe_api_reference#Loading_a_Video_Player), with one key, idiomatic difference: instead of function callbacks, you must provide event vectors to dispatch to, in the same fashion as in [http-fx](https://github.com/Day8/re-frame-http-fx)

Also note that in any map provided to this or the below effects, kebab-case will automatically be converted to camelCase as part of the js-ification process (for example, `:on-ready` and `:on-state-change` become `onReady` and `onStateChange`).

#### Methods
Each of the [methods of the `Player` object](https://developers.google.com/youtube/iframe_api_reference#Operations) have been translated into a kebab-case effect.
```clojure
(reg-event-fx
 :new-video-playing
 (fn [_ [_ video-id]]   
     {:youtube/load-video-by-id [:youtube-player video-id]}))

(reg-event-fx
 :new-video-queued
 (fn [_ [_ video-id start]]
   {:youtube/cue-video-by-id [:youtube-player {:video-id video-id   
                                               :start-seconds start}]}))

(reg-event-fx
 :current-video-playing
 (fn [_ _]
   {:youtube/play-video :youtube-player}))

(reg-event-fx
 :current-video-paused
 (fn [_ _]
   {:youtube/pause-video :youtube-player}))
```
For each effect, the same keyword provided to `:youtube/initialize-player` is required, along with any additional arguments to each method. For illustration's sake, the above translates to
```clojure
(def player (Player. "youtube-player" ...))
;; Obviously a bad place for this, especially with figwheel reloading all the time

(reg-event-fx
 :new-video-playing
 (fn [_ [_ video-id]]
   (.loadVideoById player video-id)
   {}))

(reg-event-fx
 :new-video-queued
 (fn [_ [_ video-id start]]
   (.cueVideoById player #js {:videoId videoId
                              :startSeconds start})
   {}))

(reg-event-fx
 :current-video-playing
 (fn [_ _]
   (.playVideo player)
   {}))

(reg-event-fx
 :current-video-paused
 (fn [_ _]
   (.pauseVideo player)
   {}))
```
This is obviously an extremely thin wrapper. The goal is to facilitate seemless (and *easy*, in the Rich Hickey sense) re-frame to youtube interaction, not to provide any sort of extra convenience methods or nifty new relational algegra around playing videos.

Additionally maps get translated to camelCase, same as above.

## Examples

This project contains a [very simple example](https://github.com/micmarsh/re-frame-youtube-fx/tree/master/examples/basic-player) used to illustrate video loading, playing and pausing

My [Western Music Time Machine](https://github.com/micmarsh/western-music-time-machine/blob/master/src/cljs/western_music/handlers/youtube.cljs) project contains a more complicated example, including "two way binding" of video state to UI elements by utilzing callback events.


## License

Copyright © 2023 Michael Marsh

Distributed under the Eclipse Public License version 1.0
