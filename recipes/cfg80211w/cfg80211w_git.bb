inherit deploy linux-kernel-base

DESCRIPTION = "cfg80211w"
LICENSE = "GPLv2.0-with-linux-syscall-note"
LIC_FILES_CHKSUM:sun:linux = "file://${WORKSPACE}/kernel-6.6/kernel_platform/msm-kernel/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS:prepend:sun =+ "${WORKSPACE}/kernel-6.6/kernel_platform/:"
SRC_URI = "file://msm-kernel/net/wireless"
SRC_URI:append:sun:linux = " file://msm-kernel/include/linux/ieee80211.h \
			     file://msm-kernel/include/net/cfg80211.h \
			     file://msm-kernel/include/uapi/linux/nl80211.h"
SRC_URI:append:sun:linux = " file://0001-wireless-change-Makefile-to-build-as-module-for-6.6-.patch \
	file://0002-cfg80211w-CFG80211_PROP_MULTI_LINK_SUPPORT-changes-f.patch \
	file://0001-QCLINUX-wifi-cfg80211-add-channel-frequency-conversi.patch \
	file://0002-net-Add-patch-to-increase-scan-expire-time.patch"

S = "${WORKDIR}/msm-kernel"
ADDITIONAL_CONFIGS = ""
ADDITIONAL_CONFIGS:append:sun:linux = " CFG80211_PATH=${S}"
do_configure[depends] += "virtual/kernel:do_shared_workdir"
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"
EXT_MODULES = "${@os.path.relpath("${S}/net/wireless", "${KERNEL_PLATFORM_PATH}")}"

do_compile() {
    cd ${KERNEL_PLATFORM_PATH}
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    MODULE_OUT=${S}/net/wireless \
    OUT_DIR=${WORKDIR}/out/${KERNEL_DEFCONFIG} \
    INPLACE_COMPILE=y \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    ${ADDITIONAL_CONFIGS} \
    ./build/build_module.sh
}

do_install() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    install -m 0755 ${S}/net/wireless/cfg80211w.ko -D ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/
    install ${S}/net/wireless/Module.symvers -D ${D}${base_libdir}/modules/${KERNEL_VERSION}/cfg80211w/cfg80211w.symvers
}


do_install:append:sun:linux() {
    install -d ${D}${includedir}/msm-kernel/include/linux
    install -d ${D}${includedir}/msm-kernel/include/net
    install -d ${D}${includedir}/msm-kernel/include/uapi/linux
    install -m 0644 ${WORKDIR}/msm-kernel/include/linux/ieee80211.h ${D}${includedir}/msm-kernel/include/linux/
    install -m 0644 ${WORKDIR}/msm-kernel/include/net/cfg80211.h ${D}${includedir}/msm-kernel/include/net/
    install -m 0644 ${WORKDIR}/msm-kernel/include/uapi/linux/nl80211.h ${D}${includedir}/msm-kernel/include/uapi/linux/
}

SYSROOT_DIRS:append:sun:linux = " ${includedir}/msm-kernel"
FILES:${PN}:append:sun:linux = " ${includedir}/msm-kernel"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
