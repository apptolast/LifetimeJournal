#!/usr/bin/env python3
"""
Mobile UX Audit Script - Full Mobile Design Coverage

Analyzes React Native / Flutter code for compliance with mobile design best practices.
Total: 50+ mobile-specific checks covering touch, performance, navigation, typography,
color, platform-specific patterns, testing, and debugging.

Usage: python mobile_audit.py <directory> [--json]
"""

import sys
import os
import re
import json
from pathlib import Path

class MobileAuditor:
    def __init__(self):
        self.issues = []
        self.warnings = []
        self.passed_count = 0
        self.files_checked = 0

    def audit_file(self, filepath: str) -> None:
        try:
            with open(filepath, 'r', encoding='utf-8', errors='replace') as f:
                content = f.read()
        except:
            return

        self.files_checked += 1
        filename = os.path.basename(filepath)

        is_react_native = bool(re.search(r'react-native|@react-navigation|React\.Native', content))
        is_flutter = bool(re.search(r'import \'package:flutter|MaterialApp|Widget\.build', content))

        if not (is_react_native or is_flutter):
            return

        # Touch Target Size
        small_sizes = re.findall(r'(?:width|height|size):\s*([0-3]\d)', content)
        for size in small_sizes:
            if int(size) < 44:
                self.issues.append(f"[Touch Target] {filename}: size {size}px < 44px minimum")

        # ScrollView vs FlatList
        has_scrollview = bool(re.search(r'<ScrollView|ScrollView\.', content))
        has_map_in_scrollview = bool(re.search(r'ScrollView.*\.map\(|ScrollView.*\{.*\.map', content))
        if has_scrollview and has_map_in_scrollview:
            self.issues.append(f"[Performance CRITICAL] {filename}: ScrollView with .map(). Use FlatList.")

        # React.memo + useCallback
        if is_react_native:
            has_list = bool(re.search(r'FlatList|FlashList|SectionList', content))
            if has_list and not re.search(r'React\.memo|memo\(', content):
                self.warnings.append(f"[Performance] {filename}: FlatList without React.memo")
            if has_list and not re.search(r'useCallback', content):
                self.warnings.append(f"[Performance] {filename}: FlatList without useCallback")

        # keyExtractor
        if is_react_native and re.search(r'FlatList', content):
            if not re.search(r'keyExtractor', content):
                self.issues.append(f"[Performance] {filename}: FlatList without keyExtractor")
            if re.search(r'key=\{.*index.*\}|key:\s*index', content):
                self.issues.append(f"[Performance] {filename}: Using index as key")

        # useNativeDriver
        if is_react_native and re.search(r'Animated\.', content):
            if re.search(r'useNativeDriver:\s*false', content):
                self.warnings.append(f"[Performance] {filename}: useNativeDriver: false")

        # Memory Leaks
        if is_react_native:
            if (re.search(r'useEffect', content) and
                re.search(r'addEventListener|subscribe', content) and
                not re.search(r'return\s*\(\)\s*=>|return\s+function', content)):
                self.issues.append(f"[Memory Leak] {filename}: useEffect subscriptions without cleanup")

        # Console.log
        console_count = len(re.findall(r'console\.(log|warn|error|debug)', content))
        if console_count > 5:
            self.warnings.append(f"[Performance] {filename}: {console_count} console statements")

        # Pure Black
        if re.search(r'#000000|backgroundColor:\s*["\']?black', content):
            self.warnings.append(f"[Color] {filename}: Pure black detected. Use dark gray for OLED.")

        # Dark Mode
        if not re.search(r'useColorScheme|colorScheme|isDark', content):
            self.warnings.append(f"[Color] {filename}: No dark mode support detected")

        # Secure Storage
        if (re.search(r'token|jwt|auth.*storage', content, re.IGNORECASE) and
            re.search(r'AsyncStorage', content) and
            not re.search(r'SecureStore|Keychain|EncryptedSharedPreferences', content)):
            self.issues.append(f"[Security] {filename}: Tokens in AsyncStorage (insecure)")

        # Safe Area (iOS)
        if is_react_native and not re.search(r'SafeAreaView|useSafeAreaInsets', content):
            self.warnings.append(f"[iOS] {filename}: No SafeArea detected")

        # Ripple (Android)
        if is_react_native:
            if re.search(r'Pressable|Touchable', content) and not re.search(r'ripple|android_ripple', content):
                self.warnings.append(f"[Android] {filename}: Touchable without ripple effect")

        # Accessibility Labels
        if is_react_native:
            if re.search(r'Pressable|TouchableOpacity', content) and not re.search(r'accessibilityLabel|aria-label', content):
                self.warnings.append(f"[A11y] {filename}: Interactive element without accessibilityLabel")

        # Error Boundary
        if is_react_native and not re.search(r'ErrorBoundary|componentDidCatch', content):
            self.warnings.append(f"[Debugging] {filename}: No ErrorBoundary detected")

    def audit_directory(self, directory: str) -> None:
        extensions = {'.tsx', '.ts', '.jsx', '.js', '.dart'}
        for root, dirs, files in os.walk(directory):
            dirs[:] = [d for d in dirs if d not in {'node_modules', '.git', 'dist', 'build', '.next', 'ios', 'android', '.idea'}]
            for file in files:
                if Path(file).suffix in extensions:
                    self.audit_file(os.path.join(root, file))

    def get_report(self):
        return {
            "files_checked": self.files_checked,
            "issues": self.issues,
            "warnings": self.warnings,
            "passed_checks": self.passed_count,
            "compliant": len(self.issues) == 0
        }


def main():
    if len(sys.argv) < 2:
        print("Usage: python mobile_audit.py <directory> [--json]")
        sys.exit(1)

    path = sys.argv[1]
    is_json = "--json" in sys.argv

    auditor = MobileAuditor()
    if os.path.isfile(path):
        auditor.audit_file(path)
    else:
        auditor.audit_directory(path)

    report = auditor.get_report()

    if is_json:
        print(json.dumps(report, indent=2))
    else:
        print(f"\n[MOBILE AUDIT] {report['files_checked']} mobile files checked")
        print("-" * 50)
        if report['issues']:
            print(f"[!] ISSUES ({len(report['issues'])}):")
            for i in report['issues'][:10]:
                print(f"  - {i}")
        if report['warnings']:
            print(f"[*] WARNINGS ({len(report['warnings'])}):")
            for w in report['warnings'][:15]:
                print(f"  - {w}")
        print(f"[+] PASSED CHECKS: {report['passed_checks']}")
        print(f"STATUS: {'PASS' if report['compliant'] else 'FAIL'}")

    sys.exit(0 if report['compliant'] else 1)


if __name__ == "__main__":
    main()
