(ns low.llvm
  (:require [low.jna :refer :all] :reload-all)
  (:import (com.sun.jna Pointer)))

(load "llvm_api")

(def ^:private llvm-function-map (atom {}))
(def ^:private llvm-lib (promise))

(defn import-llvm-function [f-name args ret-type]
  (swap! llvm-function-map assoc f-name
         (import-function @llvm-lib (str "LLVM" (name f-name)) args ret-type)))

(defn setup-llvm [ver]
  (deliver llvm-lib (load-lib (str "LLVM-" ver)))
  (doseq [[f-name args ret-type] llvm-api]
    (import-llvm-function f-name args ret-type)))

(defn LLVM [f & args]
  (if-let [f (@llvm-function-map f)]
    (apply f args)
    (throw (ex-info "Function not found" {:fn-name (str "LLVM" f)}))))
