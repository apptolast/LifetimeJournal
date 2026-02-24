# Android Platform Guidelines

> Material Design 3 essentials, Android design conventions, Roboto typography, and native patterns.
> **Read this file when building for Android devices.**

---

## 1. Material Design 3 Philosophy

### Core Material Principles

```
MATERIAL AS METAPHOR:
├── Surfaces exist in 3D space
├── Light and shadow define hierarchy
├── Motion provides continuity
└── Bold, graphic, intentional design

ADAPTIVE DESIGN:
├── Responds to device capabilities
├── One UI for all form factors
├── Dynamic color from wallpaper
└── Personalized per user
```

---

## 2. Android Typography (Material Type Scale)

| Role | Size | Weight | Line Height | Usage |
|------|------|--------|-------------|-------|
| **Display Large** | 57sp | Regular | 64sp | Hero text |
| **Headline Large** | 32sp | Regular | 40sp | Page titles |
| **Title Large** | 22sp | Regular | 28sp | Dialogs, cards |
| **Title Medium** | 16sp | Medium | 24sp | Lists, navigation |
| **Body Large** | 16sp | Regular | 24sp | Primary content |
| **Body Medium** | 14sp | Regular | 20sp | Secondary content |
| **Label Large** | 14sp | Medium | 20sp | Buttons, FAB |
| **Label Small** | 11sp | Medium | 16sp | Chips, badges |

RULE: ALWAYS use sp for text, dp for everything else.

---

## 3. Material Color System

### Dynamic Color (Material You - Android 12+)

User's wallpaper generates Primary, Secondary, Tertiary, and Surface colors.

### Semantic Color Roles

```
Surface Colors: Surface, SurfaceVariant, SurfaceTint, InverseSurface
On-Surface: OnSurface, OnSurfaceVariant, Outline, OutlineVariant
Primary: Primary, OnPrimary, PrimaryContainer, OnPrimaryContainer
```

### Dark Theme

```
Background: #121212 (not pure black by default)
Elevation overlays: Higher = lighter
```

---

## 4. Android Layout & Spacing

### 8dp Baseline Grid

All spacing in multiples of 8dp: 4dp (half-step), 8dp, 16dp, 24dp, 32dp

### Window Size Classes

- **Compact** (< 600dp): Phones, single column, bottom navigation
- **Medium** (600-840dp): Tablets/foldables, navigation rail
- **Expanded** (> 840dp): Large tablets, navigation drawer

---

## 5. Android Navigation Patterns

| Component | Use Case | Position |
|-----------|----------|----------|
| **Bottom Navigation** | 3-5 top-level destinations | Bottom (80dp) |
| **Navigation Rail** | Tablets, foldables | Left side (80dp wide) |
| **Navigation Drawer** | Many destinations | Left side |
| **Top App Bar** | Context, actions | Top (64dp small) |

### Back Navigation

Android provides system back: back button, back gesture, predictive back (Android 14+). Support all correctly.

---

## 6. Material Components

### Buttons

- Filled: Primary action
- Tonal: Secondary
- Outlined: Tertiary
- Text: Lowest emphasis
- Min touch target: 48dp

### FAB: 56dp standard, bottom right, 16dp from edges

### Cards: Elevated/Filled/Outlined, 12dp corner radius, 16dp padding

### Text Fields: Filled (underline) or Outlined, 56dp height, floating label

### Bottom Sheets: Standard or Modal, 28dp corner radius (top)

---

## 7. Android-Specific Patterns

- **Snackbar**: Bottom, 4-10s, one action, swipeable
- **Ripple Effect**: MANDATORY on every touchable element
- **Pull to Refresh**: Circular material indicator

---

## 8. Material Symbols

Styles: Outlined (default), Rounded, Sharp. Sizes: 20dp, 24dp (standard), 40dp, 48dp.

---

## 9. Android Accessibility

- Touch targets: 48dp x 48dp MANDATORY
- TalkBack: contentDescription on all interactive elements
- Font scaling: Test at 200%
- Reduce motion: Check ANIMATOR_DURATION_SCALE

---

## 10. Android Checklist

### Before Every Screen
- [ ] Material 3 components
- [ ] Touch targets >= 48dp
- [ ] Ripple on all touchables
- [ ] Material type scale
- [ ] Semantic colors

### Before Release
- [ ] Dark theme tested
- [ ] Dynamic color tested
- [ ] Font scaling 200% tested
- [ ] TalkBack tested
- [ ] Predictive back (Android 14+)
- [ ] Edge-to-edge (Android 15+)

---

> **Remember:** Android users expect Material Design. Custom designs that ignore Material patterns feel foreign and broken.
