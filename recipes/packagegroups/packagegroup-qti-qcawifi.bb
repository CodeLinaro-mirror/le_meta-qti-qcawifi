SUMMARY = "QCA WIFI package opensource groups"
LICENSE = "BSD-3-Clause"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = "packagegroup-qti-qcawifi"

RDEPENDS:packagegroup-qti-qcawifi = " \
         iw \
         roxml \
         wireless-tools \
	 ${@bb.utils.contains('BASEMACHINE', 'qcs40x', '', 'qca-cnss', d)} \
         "
