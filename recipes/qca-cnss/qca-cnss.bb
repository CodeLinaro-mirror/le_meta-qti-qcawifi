DESCRIPTION = "QCA CNSS Wifi Platform Driver"

FILESPATH =+ "${WORKSPACE}:"
FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

inherit autotools-brokensep module qperf

do_unpack[deptask] = "do_populate_sysroot"
DEPENDS = "virtual/kernel"
SRC_URI = "file://qcawifi/qca-cnss"
SRC_URI += "file://blacklist-qcacnss.conf"

S = "${WORKDIR}/qcawifi/qca-cnss"

FILES_${PN}+="/usr/lib/modules/${KERNEL_VERSION}/extra/"
FILES_${PN}+="/etc/modprobe.d/*"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

EXTRA_OECONF += "--with-kernel-src=${STAGING_KERNEL_DIR} \
                 --with-kernel=${STAGING_KERNEL_BUILDDIR} \
                 --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

export CONFIG_BUILD_YOCTO="y"

do_install_append() {
	install -m 0755 -d ${D}/usr/include
	cp ${S}/include/cnss2.h ${D}/usr/include/
	install -d ${D}/etc/modprobe.d/
	install -m 0644 ${WORKDIR}/blacklist-qcacnss.conf ${D}/etc/modprobe.d/
}
