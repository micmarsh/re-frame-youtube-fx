# re-frame-youtube-fx

A re-frame library that integrates the YouTube iframe API into re-frame effects, and handles management of global player instances.

## Usage

### Dependency
```clojure
:dependencies [[re-frame-youtube-fx "0.1.0-SNAPSHOT"]]
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
The first argument to the vector (in this case `:youtube-player`) must be a keyword indicating the id of the html element to attach the player to, akin to the [first argument of the `Player` constructor]().

The second map of options is also equivalent to the [options argument of the `Player` constructor](), with one key, idiomatic difference: instead of function callbacks, you must provide event vectors to dispatch to, in the same fashion as in [http-fx]()

Also note that in any map provided to this or the below effects, kebab case will automatically be converted to camel case as part of the js-ification process (for example, `:on-ready` and `:on-state-change` become `onReady` and `onStateChange`).


TODO: explain how things wrap the whole api


FIXME

## License

Copyright © 2016 FIXME

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
