#!/bin/bash

docker run -v /home/admin/daplsw/docker/cran-server/packages:/srv/cran cran-server:1.0.1 bash -c "R -f /update_PACKAGES.R"