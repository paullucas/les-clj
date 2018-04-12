(ns les.core-test
  (:require [les.core :as sut]
            #?(:clj [clojure.test :as t]
               :cljs [cljs.test :as t :include-macros true :refer-macros [deftest is]])))
