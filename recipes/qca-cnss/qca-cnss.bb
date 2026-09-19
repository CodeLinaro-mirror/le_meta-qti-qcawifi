inherit linux-kernel-base deploy

DESCRIPTION = "QCA CNSS Wifi Platform Driver"

FILESPATH =+ "${WORKSPACE}:"
FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

do_unpack[deptask] = "do_populate_sysroot"
DEPENDS = "virtual/kernel"
SRC_URI = "file://qcawifi/qca-cnss"
SRC_URI += "file://blacklist-qcacnss.conf"

PACKAGE_ARCH  ?= "${MACHINE_ARCH}"
S = "${UNPACKDIR}/qcawifi/qca-cnss"

FILES:${PN} += "/usr/include/*"
FILES:${PN} += "/etc/modprobe.d/*"
FILES:${PN} += "lib/modules/${KERNEL_VERSION}/extra/*.ko"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"
EXTRA_OEMAKE += "KBUILD_EXTRA_SYMBOLS=${KBUILD_EXTRA_SYMBOLS}"
EXTRA_OEMAKE += "CONFIG_CNSS_OUT_OF_TREE=y"
EXTRA_OEMAKE += "BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG}"

EXTRA_OECONF += "--with-kernel-src=${STAGING_KERNEL_DIR} \
                 --with-kernel=${STAGING_KERNEL_BUILDDIR} \
                 --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

export CONFIG_BUILD_YOCTO="y"

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"
EXT_MODULES = "${@os.path.relpath("${S}", "${KERNEL_PLATFORM_PATH}")}"

MODULE_NAME = "qca_cnss2.ko"

do_compile[depends] += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${WORKDIR}/out/${KERNEL_DEFCONFIG}"

do_compile() {
    cd ${KERNEL_PLATFORM_PATH}
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    MODULE_OUT=${S} \
    OUT_DIR=${WORKDIR}/out/${KERNEL_DEFCONFIG} \
    INPLACE_COMPILE=y \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    ./build/build_module.sh
}

do_install:append() {
	install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
        install -m 0755 ${S}/${MODULE_NAME} ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
	install -m 0755 -d ${D}/usr/include
	cp ${S}/include/cnss2.h ${D}/usr/include/
	install -d ${D}/etc/modprobe.d/
	install -m 0644 ${WORKDIR}/blacklist-qcacnss.conf ${D}/etc/modprobe.d/
}

do_deploy() {
    install -d ${DEPLOYDIR}/kernel_modules
    install -m 0755 ${S}/${MODULE_NAME} ${DEPLOYDIR}/kernel_modules
}

addtask do_deploy after do_install
