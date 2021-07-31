#!/bin/bash
set -eu

ab -c 10 -n $1 -k http://localhost:1234/product/completablefuture/nexus

