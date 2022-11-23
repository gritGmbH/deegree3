| Main (3.5)   | Maintenance (3.4) | Release   | OpenHUB       |
| ------------ | ----------------- | --------- | ------------- |
| [![Main Build Status](https://buildserver.deegree.org/buildStatus/icon?job=deegree-3.5-release)](https://buildserver.deegree.org/job/deegree-3.5-release/) | [![Master Build Status](https://buildserver.deegree.org/buildStatus/icon?job=deegree-3.4%2Fdeegree-3.4-master)](https://buildserver.deegree.org/job/deegree-3.4/job/deegree-3.4-master/) | [![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/deegree/deegree3?sort=semver)](https://github.com/deegree/deegree3/releases/latest) | [![OpenHUB](https://www.openhub.net/p/deegree3/widgets/project_thin_badge.gif)](https://www.openhub.net/p/deegree3) |
# deegree webservices
deegree is open source software for spatial data infrastructures and the geospatial web. deegree includes components for geospatial data management, including data access, visualization, discovery and security. Open standards are at the heart of deegree. The software is built on the standards of the Open Geospatial Consortium (OGC) and the ISO Technical Committee 211.

## User documentation
General project information and user documentation (e.g. "How to set up WMS and WFS?" or "How to get support?") can be found on the deegree homepage:

https://www.deegree.org/documentation

## Developer documentation
Information for developer (e.g. "How to build deegree webservices?") can be found on the deegree project wiki on GitHub:

https://github.com/deegree/deegree3/wiki

## License

[![License](https://img.shields.io/badge/License-LGPL%20v2.1-blue.svg)](https://www.gnu.org/licenses/lgpl-2.1)

deegree is distributed under the GNU Lesser General Public License, Version 2.1 (LGPL 2.1). Generally speaking this means that you have essential freedoms such as: Run the software for any purpose, find out how it works, make changes, redistribute copies (of modified versions). Practically, with these freedoms, you do not have to pay a license fee, you can create as many installations as you need, and you're not bound to one single vendor. Instead you can contact a service provider of your choice to make necessary configurations or code adjustments. Or you may even step up and do this yourself.

More information about free software can be found at the free software foundation. A good starting point is [www.gnu.org](https://www.gnu.org).

## Contribution guidelines

First off all, thank you for taking the time to contribute to deegree! :+1: :tada:

By participating you are expected to uphold the [Contribution guidelines](CONTRIB.md).

You can add yourself when contributing to the project to the [list of contributors](CONTRIBUTORS.md).

## Changes in respect of upstream project

This Branch includes, in difference to https://github.com/deegree/deegree3/tree/main, the following pull requests.
<!-- branchlist -->
<!--
//- 1090 [Fixed html.gfi to avoid same gml id for all features](https://github.com/deegree/deegree3/pull/1090)
//  - lat-lon:duplicateIdsWithDefaultHtmlGfi-6802
-->
<!--
//- 1167 [Refactored import statements to use org.junit.Assert ](https://github.com/deegree/deegree3/pull/1167)
//  - lat-lon:fix/deprecatedJunit
-->
- 1291 [Acceleration of batch import jobs by enabling the use of sql batch operations](https://github.com/deegree/deegree3/pull/1291)
  - gritGmbH:enhancement/optimize-batch-load
- 1293 [Extend the loader tool with the option to print a statics over each processed feature type](https://github.com/deegree/deegree3/pull/1293)
  - gritGmbH:enhancement/gml-tool-add-statistics
- 1390 [Make report file configurable in the loader tool](https://github.com/deegree/deegree3/pull/1390)
  - gritGmbH:enhancement/gml-tool-configurable-report-file
- 1391 [Allow disabling trimming of simple property values in GML reader](https://github.com/deegree/deegree3/pull/1391)
  - gritGmbH:enhancement/gml-reader-whitespace-trim-configurable
- 1395 [Make decimal places of numeric values in GetFeatureInfo for cover configurable](https://github.com/deegree/deegree3/pull/1395)
  - gritGmbH:enhancement/cov-gfi-configurable-rounding-TT_10331
<!--
//- 1396 [Cleanup API for MapOptions](https://github.com/deegree/deegree3/pull/1396)
//  - gritGmbH:enhancement/cleanup-mapoptions-api
//  * Cleanup after 1395, not needed
-->
- 1402 [Prevent serialization of exceptions with "null" message](https://github.com/deegree/deegree3/pull/1402)
  - gritGmbH:enhancement/fix-null-exception-serializer-85
- 1404 [Extending the Oracle SQL dialect to allow reading of oriented points](https://github.com/deegree/deegree3/pull/1404)
  - gritGmbH:enhancement/correct-oriented-point-dee-194
- 1406 [Adds endpoint to REST API to list available fonts on the server](https://github.com/deegree/deegree3/pull/1406)
  - gritGmbH:enhancement/rest-api-add-fontlist-83
- 1407 [Add support for rectangular (box-styled) halo](https://github.com/deegree/deegree3/pull/1407)
  - gritGmbH:enhancement/box-styled-halo-83
- 1410 [Extending the SLD/SE rendering capabilities of Mark for more symbols and better area fill](https://github.com/deegree/deegree3/pull/1410)
  - gritGmbH:enhancement/se-sld-styling-imporvements-83-289
- 1411 [Extending the SLD/SE rendering capabilities of Mark for symbols based on SVG paths](https://github.com/deegree/deegree3/pull/1411)
  - gritGmbH:enhancement/mark-type-svgpath-83-289
- 1414 [Enable support for 4-band raster data with GdalLayer](https://github.com/deegree/deegree3/pull/1414)
  - gritGmbH:enhancement/four-band-gdallayer-rework-pr-1067
- 1417 [Register True-Type fonts at workspace startup](https://github.com/deegree/deegree3/pull/1417)
  - gritGmbH:enhancement/add-font-loader
- 1419 [Remove jettison from dependencies](https://github.com/deegree/deegree3/pull/1419)
  - gritGmbH:enhancement/remove-jettison
- 1421 [Make last glyph of font reachable for point rendering](https://github.com/deegree/deegree3/pull/1421)
  - gritGmbH:bugfix/last-font-symbol-unreachable
- 1424 [Fix conversation of foot within unit of measure handling](https://github.com/deegree/deegree3/pull/1424)
  - gritGmbH:bugfix/unit-of-measure-foot
- 1429 [Update Oracle driver to 19.9.0.0](https://github.com/deegree/deegree3/issues/1429)
  - gritGmbH:enhancement/oracle-driver-update
<!--
//- 1425 [Update the documentation of deegree](https://github.com/deegree/deegree3/pull/1425)
//  - lat-lon:updateDocs8440-8455
//- 1426 [Update SECURITY.md](https://github.com/deegree/deegree3/pull/1426)
//  - deegree:tfr42-updateSecurityPolicy
-->
- 1427 [Extending the SLD/SE rendering capabilities of Mark for symbols based on Well Known Text (WKT) geometries](https://github.com/deegree/deegree3/pull/1427)
  - gritGmbH:enhancement/mark-type-wkt-83-289
- 1428 [Speed up transcoding SVG to Image](https://github.com/deegree/deegree3/pull/1428)
  - gritGmbH:enhancement/speedup-svg-transcoding

<!-- endbranchlist -->