SUMMARY = "QCA WIFI package opensource groups"
LICENSE = "BSD-3-Clause"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = "packagegroup-qti-qcawifi"

RDEPENDS_packagegroup-qti-qcawifi = " \
         iw \
         "
