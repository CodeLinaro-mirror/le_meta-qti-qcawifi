FILESEXTRAPATHS_append := "${THISDIR}:${THISDIR}/patches:"

SRC_URI += "file://0001-ANDROID-GKI-arm64-provide-dma-cache-routines.patch \
            file://0002-cfg80211-obtain-sae_pwe-value-from-userspace.patch \
            file://0003-cfg80211-AP-mode-driver-offload-for-FILS-association.patch \
            file://0001-net-Add-chan-to-freq-formula-for-2.5MHz-step-chan.patch \
            file://0004-arm64-export-bss-section-symbols.patch \
            file://0002-net-Add-chan-to-freq-formula-for-new-added-channel.patch \
            file://0005-net-Change-scan-expire-time-to-180s.patch \
"
