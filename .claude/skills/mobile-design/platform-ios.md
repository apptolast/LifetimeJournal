# iOS Platform Guidelines

> Human Interface Guidelines (HIG) essentials, iOS design conventions, SF Pro typography, and native patterns.
> **Read this file when building for iPhone/iPad.**

---

## 1. Human Interface Guidelines Philosophy

### Core Apple Design Principles

```
CLARITY: Text legible, icons precise, adornments subtle
DEFERENCE: UI helps understand, content fills screen
DEPTH: Distinct visual layers convey hierarchy
```

---

## 2. iOS Typography

### SF Pro Font Family

```
iOS System Fonts:
├── SF Pro Text: Body text (< 20pt)
├── SF Pro Display: Large titles (≥ 20pt)
├── SF Pro Rounded: Friendly contexts
├── SF Mono: Code, tabular data
```

### iOS Type Scale (Dynamic Type)

| Style | Default Size | Weight | Usage |
|-------|--------------|--------|-------|
| **Large Title** | 34pt | Bold | Navigation bar collapse |
| **Title 1** | 28pt | Bold | Page titles |
| **Title 2** | 22pt | Bold | Section headers |
| **Title 3** | 20pt | Semibold | Subsection headers |
| **Headline** | 17pt | Semibold | Emphasized body |
| **Body** | 17pt | Regular | Primary content |
| **Callout** | 16pt | Regular | Secondary content |
| **Subhead** | 15pt | Regular | Tertiary content |
| **Footnote** | 13pt | Regular | Captions |
| **Caption 1** | 12pt | Regular | Annotations |
| **Caption 2** | 11pt | Regular | Fine print |

### Dynamic Type Support (MANDATORY)

```swift
// ✅ CORRECT: Dynamic Type
Text("Hello").font(.body) // Scales with user settings
```

---

## 3. iOS Color System

### System Colors (Semantic) - Auto dark mode

```
Primary: .label, .secondaryLabel, .tertiaryLabel
Backgrounds: .systemBackground, .secondarySystemBackground
Fills: .systemFill, .secondarySystemFill
```

### System Accent Colors

| Color | Light | Dark | Usage |
|-------|-------|------|-------|
| Blue | #007AFF | #0A84FF | Links, default tint |
| Green | #34C759 | #30D158 | Success |
| Red | #FF3B30 | #FF453A | Errors, destructive |
| Orange | #FF9500 | #FF9F0A | Warnings |

---

## 4. iOS Layout & Spacing

### Safe Areas

Never place interactive content in unsafe areas (status bar, home indicator).

### Standard Margins

- Screen edge: 16pt
- List item padding: 16pt horizontal
- Card padding: 16pt

---

## 5. iOS Navigation Patterns

| Pattern | Use Case |
|---------|----------|
| **Tab Bar** | 3-5 top-level sections (49pt height) |
| **Navigation Controller** | Hierarchical drill-down |
| **Modal/Sheet** | Focused task |
| **Sidebar** | iPad multi-column |

### Gestures

- Edge swipe left: Navigate back
- Pull down sheet: Dismiss modal
- Long press: Context menu

---

## 6. iOS Components

### Buttons: Tinted (primary), Bordered (secondary), Plain (tertiary)
### Lists: .insetGrouped (default iOS 14+), disclosure indicators
### Sheets: .medium (half), .large (full), custom detents
### Context Menus: Long press, preview + actions, destructive last in red

---

## 7. SF Symbols

5000+ icons. Match text weight. Scales: .small, .medium, .large.

```swift
Image(systemName: "star.fill")
    .font(.title2)
    .foregroundStyle(.yellow)
```

---

## 8. iOS Accessibility

### VoiceOver

All interactive elements need: accessibilityLabel, traits, value.

### Dynamic Type Scaling

Users set from xSmall (14pt body) to Accessibility sizes (53pt). App MUST scale gracefully.

### Reduce Motion

```swift
@Environment(\.accessibilityReduceMotion) var reduceMotion
```

---

## 9. iOS Checklist

### Before Every Screen
- [ ] SF Pro or SF Symbols
- [ ] Dynamic Type supported
- [ ] Safe areas respected
- [ ] Back gesture works
- [ ] Tab bar items <= 5
- [ ] Touch targets >= 44pt

### Before Release
- [ ] Dark mode tested
- [ ] All text sizes tested
- [ ] VoiceOver tested
- [ ] Edge swipe back works
- [ ] Keyboard avoidance
- [ ] Notch/Dynamic Island handled
- [ ] Home indicator respected

---

> **Remember:** iOS users have strong expectations from other iOS apps. Deviating from HIG patterns feels "broken." When in doubt, use the native component.
