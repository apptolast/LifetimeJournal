# Mobile Typography Reference

> Type scale, system fonts, Dynamic Type, accessibility, and dark mode typography.
> **Typography failures are the #1 cause of unreadable mobile apps.**

---

## 1. Mobile Typography Fundamentals

### Why Mobile Type is Different

```
DESKTOP:                        MOBILE:
├── 20-30" viewing distance     ├── 12-15" viewing distance
├── Large viewport              ├── Small viewport, narrow
├── Hover for details           ├── Tap/scroll for details
├── Controlled lighting         ├── Variable (outdoor, etc.)
├── Fixed font size             ├── User-controlled sizing
└── Long reading sessions       └── Quick scanning
```

### Mobile Type Rules

| Rule | Desktop | Mobile |
|------|---------|--------|
| **Minimum body size** | 14px | 16px (14pt/14sp) |
| **Maximum line length** | 75 characters | 40-60 characters |
| **Line height** | 1.4-1.5 | 1.4-1.6 (more generous) |
| **Font weight** | Varies | Regular dominant, bold sparingly |
| **Contrast** | AA (4.5:1) | AA minimum, AAA preferred |

---

## 2. System Fonts

### iOS: SF Pro Family

```
San Francisco (SF) Family:
├── SF Pro Display: Large text (≥ 20pt)
├── SF Pro Text: Body text (< 20pt)
├── SF Pro Rounded: Friendly contexts
├── SF Mono: Monospace
└── SF Compact: Apple Watch, compact UI
```

### Android: Roboto Family

```
Roboto Family:
├── Roboto: Default sans-serif
├── Roboto Flex: Variable font
├── Roboto Serif: Serif option
├── Roboto Mono: Monospace
├── Roboto Condensed: Narrow spaces
```

### When to Use System Fonts

```
✅ USE system fonts when:
├── Brand doesn't mandate custom font
├── Reading efficiency is priority
├── App feels native/integrated important
├── Performance is critical
├── Wide language support needed

❌ AVOID system fonts when:
├── Brand identity requires custom
├── Design differentiation needed
├── Editorial/magazine style
└── (But still support accessibility)
```

---

## 3. Type Scale

### iOS Type Scale (Built-in)

| Style | Size | Weight | Line Height |
|-------|------|--------|-------------|
| Large Title | 34pt | Bold | 41pt |
| Title 1 | 28pt | Bold | 34pt |
| Title 2 | 22pt | Bold | 28pt |
| Title 3 | 20pt | Semibold | 25pt |
| Headline | 17pt | Semibold | 22pt |
| Body | 17pt | Regular | 22pt |
| Callout | 16pt | Regular | 21pt |
| Subhead | 15pt | Regular | 20pt |
| Footnote | 13pt | Regular | 18pt |
| Caption 1 | 12pt | Regular | 16pt |
| Caption 2 | 11pt | Regular | 13pt |

### Android Type Scale (Material 3)

| Role | Size | Weight | Line Height |
|------|------|--------|-------------|
| Display Large | 57sp | 400 | 64sp |
| Display Medium | 45sp | 400 | 52sp |
| Display Small | 36sp | 400 | 44sp |
| Headline Large | 32sp | 400 | 40sp |
| Headline Medium | 28sp | 400 | 36sp |
| Headline Small | 24sp | 400 | 32sp |
| Title Large | 22sp | 400 | 28sp |
| Title Medium | 16sp | 500 | 24sp |
| Title Small | 14sp | 500 | 20sp |
| Body Large | 16sp | 400 | 24sp |
| Body Medium | 14sp | 400 | 20sp |
| Body Small | 12sp | 400 | 16sp |
| Label Large | 14sp | 500 | 20sp |
| Label Medium | 12sp | 500 | 16sp |
| Label Small | 11sp | 500 | 16sp |

---

## 4. Dynamic Type / Text Scaling

### iOS Dynamic Type (MANDATORY)

```swift
// ❌ WRONG: Fixed size
Text("Hello").font(.system(size: 17))

// ✅ CORRECT: Dynamic Type
Text("Hello").font(.body)
```

### Android Text Scaling (MANDATORY)

```
ALWAYS use sp for text:
├── sp = Scale-independent pixels
├── Scales with user font preference
├── dp does NOT scale (don't use for text)

User can scale from 85% to 200%
Test at 200%!
```

---

## 5. Typography Accessibility

### Minimum Sizes

| Element | Minimum | Recommended |
|---------|---------|-------------|
| Body text | 14px/pt/sp | 16px/pt/sp |
| Secondary text | 12px/pt/sp | 13-14px/pt/sp |
| Captions | 11px/pt/sp | 12px/pt/sp |
| Buttons | 14px/pt/sp | 14-16px/pt/sp |

### Contrast Requirements (WCAG)

```
Normal text (< 18pt or < 14pt bold): AA: 4.5:1 minimum
Large text (≥ 18pt or ≥ 14pt bold): AA: 3:1 minimum
```

---

## 6. Dark Mode Typography

### Color Adjustments

| Level | Light Mode | Dark Mode |
|-------|------------|-----------|
| Primary text | #000000 | #E8E8E8 |
| Secondary text | #666666 | #A0A0A0 |
| Tertiary text | #999999 | #707070 |
| Disabled text | #CCCCCC | #505050 |

### Weight in Dark Mode

```
Dark mode text appears thinner due to halation.
Consider using medium weight for body instead of regular.
```

---

## 7. Typography Anti-Patterns

| Mistake | Problem | Fix |
|---------|---------|-----|
| Fixed font sizes | Ignores accessibility | Use dynamic sizing |
| Too small text | Unreadable | Min 14pt/sp |
| Low contrast | Invisible in sunlight | Min 4.5:1 |
| Long lines | Hard to track | Max 60 chars |
| Tight line height | Cramped | Min 1.4x |
| Too many sizes | Visual chaos | Max 5-7 sizes |

---

> **Remember:** If users can't read your text, your app is broken. Typography isn't decoration—it's the primary interface. Test on real devices, in real conditions, with accessibility settings enabled.
