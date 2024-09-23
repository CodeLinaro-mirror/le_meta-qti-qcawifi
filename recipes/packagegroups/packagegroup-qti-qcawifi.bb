SUMMARY = "QCA WIFI package opensource groups"
LICENSE = "BSD-3-Clause"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

CFG80211W = ""
CFG80211W:append:kalama:ubuntu = "cfg80211w"

PACKAGES = "packagegroup-qti-qcawifi"

RDEPENDS:packagegroup-qti-qcawifi = " \
         iw-w \
         roxml \
         ${@bb.utils.contains('BASEMACHINE', 'qcs40x', '', 'qca-cnss', d)} \
         ${CFG80211W} \
         "
