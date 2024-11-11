inherit deploy linux-kernel-base

DESCRIPTION = "cfg80211w"
LICENSE = "GPL-2.0 WITH Linux-syscall-note"
LIC_FILES_CHKSUM = "file://${WORKSPACE}/kernel-5.15/kernel_platform/msm-kernel/COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESPATH =+ "${WORKSPACE}/kernel-5.15/kernel_platform/:"
SRC_URI = "file://msm-kernel/net/wireless"
SRC_URI += "file://0001-wireless-change-Makefile-to-build-as-module.patch"

S = "${WORKDIR}/msm-kernel"
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
    ./build/build_module.sh
}

do_install() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}
    install -m 0755 ${S}/net/wireless/cfg80211w.ko -D ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/
    install ${S}/net/wireless/Module.symvers -D ${D}${base_libdir}/modules/${KERNEL_VERSION}/cfg80211w/cfg80211w.symvers
}

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/*"
