SUMMARY = "QCA WIFI package opensource groups"
LICENSE = "BSD-3-Clause"
PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

CFG80211W = ""
CFG80211W:append:sun:linux = "cfg80211w"

PACKAGES = "packagegroup-qti-qcawifi"

RDEPENDS:packagegroup-qti-qcawifi = " \
         tcpdump \
         iw-w \
         roxml \
         qca-cnss \
         ${CFG80211W} \
         "
