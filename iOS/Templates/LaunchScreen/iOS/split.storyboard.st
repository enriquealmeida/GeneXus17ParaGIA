    <scenes>
        <!--View Controller-->
        <scene sceneID="EnR-Hv-Huh">
            <objects>
                <viewController id="Y17-hX-q5Z" sceneMemberID="viewController">
                    <view key="view" contentMode="scaleToFill" id="i2x-2Q-dlM">
                        <autoresizingMask key="autoresizingMask" widthSizable="YES" heightSizable="YES"/>
                        <color key="backgroundColor" systemColor="systemBackgroundColor" cocoaTouchSystemColor="whiteColor"/>
                        <viewLayoutGuide key="safeArea" id="S6r-HH-kTS"/>
                    </view>
                    <navigationItem key="navigationItem" id="W9O-Ws-b9O"/>
                </viewController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="5pm-7j-ehc" userLabel="First Responder" customClass="UIResponder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="764" y="656"/>
        </scene>
        <!--Navigation Controller-->
        <scene sceneID="bNH-9K-gIv">
            <objects>
                <navigationController id="wEG-fM-x9A" sceneMemberID="viewController">
                    <navigationBar key="navigationBar" contentMode="scaleToFill" insetsLayoutMarginsFromSafeArea="NO" id="uwB-Jp-frT">
                        <autoresizingMask key="autoresizingMask"/>
                    </navigationBar>
                    <connections>
                        <segue destination="Y17-hX-q5Z" kind="relationship" relationship="rootViewController" id="EZ0-Kz-4oi"/>
                    </connections>
                </navigationController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="9gf-Uo-wRN" userLabel="First Responder" customClass="UIResponder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="66" y="656"/>
        </scene>
        <!--View Controller-->
        <scene sceneID="qkH-3f-5Yl">
            <objects>
                <viewController id="ya2-Vh-2QN" sceneMemberID="viewController">
                    <view key="view" contentMode="scaleToFill" id="0Xm-FR-HtC">
                        <autoresizingMask key="autoresizingMask" widthSizable="YES" heightSizable="YES"/>
                        <color key="backgroundColor" systemColor="systemBackgroundColor" cocoaTouchSystemColor="whiteColor"/>
                        <viewLayoutGuide key="safeArea" id="8Ng-8O-cu4"/>
                    </view>
                    <navigationItem key="navigationItem" id="Ckv-nm-2tK"/>
                </viewController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="ZYh-uI-HiR" userLabel="First Responder" customClass="UIResponder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="$if(LaunchScreen.MainIsMenuTab)$1446$else$720$endif$" y="-48"/>
        </scene>
        <!--Navigation Controller-->
        <scene sceneID="GOn-lS-VuJ">
            <objects>
                <navigationController id="ktk-pJ-ct3" sceneMemberID="viewController">
$if(LaunchScreen.MainIsMenuTab)$
                    <tabBarItem key="tabBarItem" title="" id="XxX-jJ-lEQ"/>
$endif$
                    <navigationBar key="navigationBar" contentMode="scaleToFill" insetsLayoutMarginsFromSafeArea="NO" id="xjv-rS-Vnl">
                        <autoresizingMask key="autoresizingMask"/>
                    </navigationBar>
                    <connections>
                        <segue destination="ya2-Vh-2QN" kind="relationship" relationship="rootViewController" id="ba7-rN-6kd"/>
                    </connections>
                </navigationController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="O5b-Lf-gi1" userLabel="First Responder" customClass="UIResponder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="$if(LaunchScreen.MainIsMenuTab)$764$else$67$endif$" y="-48"/>
        </scene>
        <!--Split View Controller-->
        <scene sceneID="En6-fm-ank">
            <objects>
                <splitViewController id="$LaunchScreen.InitialViewControllerId$" sceneMemberID="viewController">
                    <connections>
                        <segue destination="$if(LaunchScreen.MainIsMenuTab)$Lo6-yJ-BgM$else$ktk-pJ-ct3$endif$" kind="relationship" relationship="masterViewController" id="t1Q-ID-qbh"/>
                        <segue destination="wEG-fM-x9A" kind="relationship" relationship="detailViewController" id="TlJ-XE-jJI"/>
                    </connections>
                </splitViewController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="pD9-JD-NAM" userLabel="First Responder" customClass="UIResponder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="-703" y="274"/>
        </scene>
$if(LaunchScreen.MainIsMenuTab)$
        <!--Tab Bar Controller-->
        <scene sceneID="xth-14-izU">
            <objects>
                <tabBarController id="Lo6-yJ-BgM" sceneMemberID="viewController">
                    <tabBar key="tabBar"$if(LaunchScreen.TabBarHidden)$ hidden="YES"$endif$ contentMode="scaleToFill" insetsLayoutMarginsFromSafeArea="NO" id="ag2-Df-IHc">
                        <autoresizingMask key="autoresizingMask"/>
                        <color key="tintColor" white="0.0" alpha="0.0" colorSpace="custom" customColorSpace="genericGamma22GrayColorSpace"/>
                        <color key="selectedImageTintColor" white="0.0" alpha="0.0" colorSpace="custom" customColorSpace="genericGamma22GrayColorSpace"/>
$if(LaunchScreen.TabBarHiddenVariationOniPad)$
                        <variation key="heightClass=regular-widthClass=regular" hidden="$if(LaunchScreen.TabBarHidden)$NO$else$YES$endif$"/>
$endif$
                    </tabBar>
                    <connections>
                        <segue destination="ktk-pJ-ct3" kind="relationship" relationship="viewControllers" id="e8Q-3N-hj8"/>
                    </connections>
                </tabBarController>
                <placeholder placeholderIdentifier="IBFirstResponder" id="j7B-ub-RR8" userLabel="First Responder" customClass="UIResponder" sceneMemberID="firstResponder"/>
            </objects>
            <point key="canvasLocation" x="65" y="-48"/>
        </scene>
$endif$
    </scenes>
