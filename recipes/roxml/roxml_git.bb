inherit autotools-brokensep pkgconfig qprebuilt

DESCRIPTION = "ROXML Library"

HOMEPAGE    = "http://www.libroxml.net/"
LICENSE     = "LGPL-2.1+"
LIC_FILES_CHKSUM += "file://License.txt;md5=81cba52d2de829c8be3d167618e6b8b6"

FILES:${PN} += "/usr/lib/*"
FILES:${PN} += "/usr/include/*.h"
FILES_SOLIBSDEV = ""
INSANE_SKIP:${PN} += "dev-so installed-vs-shipped"

PKG_NAME = "libroxml"
PKG_VERSION = "2.3.0"

SRC_URI = "http://download.libroxml.net/pool/v2.x/${PKG_NAME}-${PKG_VERSION}.tar.gz;name=libtar"

SRC_URI[libtar.md5sum] = "a975f91be150f7a19168a45ce15769ca"

S = "${WORKDIR}/libroxml-2.3.0"

PACKAGE_ARCH  ?= "${MACHINE_ARCH}"
PR = "r1"

EXTRA_OECONF += "--enable-shared --enable-static --disable-roxml"

