# Touch Psychology Reference

> Deep dive into mobile touch interaction, Fitts' Law for touch, thumb zone anatomy, gesture psychology, and haptic feedback.
> **CRITICAL for all mobile work.**

---

## 1. Fitts' Law for Touch

### The Fundamental Difference

```
DESKTOP (Mouse): 1px precision, hover states, low error cost
MOBILE (Finger): ~7mm contact, no hover, high error cost, occlusion
```

### Minimum Touch Target Sizes

| Platform | Minimum | Recommended |
|----------|---------|-------------|
| **iOS (HIG)** | 44pt x 44pt | 48pt+ |
| **Android (Material)** | 48dp x 48dp | 56dp+ |
| **WCAG 2.2** | 44px x 44px | - |

### Visual Size vs Hit Area

Visual can be smaller (e.g., 24px icon) if hit area is padded to 44-48px minimum.

---

## 2. Thumb Zone Anatomy

### One-Handed Phone Usage (49% of users)

```
┌─────────────────────────────┐
│      HARD TO REACH          │ ← Navigation, menu, back
├─────────────────────────────┤
│      OK TO REACH            │ ← Secondary actions, content
├─────────────────────────────┤
│      EASY TO REACH          │ ← PRIMARY CTAs, tab bar
└─────────────────────────────┘
        [  HOME  ]
```

### Placement Guidelines

| Element | Position | Reason |
|---------|----------|--------|
| Primary CTA | Bottom center/right | Easy thumb reach |
| Tab bar | Bottom | Natural position |
| FAB | Bottom right | Easy for right hand |
| Destructive | Top left | Hard to reach = safety |
| Navigation | Top | Less frequent use |

---

## 3. Touch Feedback Requirements

```
Tap → Immediate visual change (< 50ms)
├── Highlight, scale, ripple, or haptic
├── Never nothing!
Loading → Show within 100ms
├── Spinner, disable button, optimistic UI
```

---

## 4. Gesture Psychology

### Discoverability Problem

Gestures are INVISIBLE. Always provide visible alternative.

| Gesture | Meaning | Always provide |
|---------|---------|----------------|
| Swipe to delete | Remove item | Delete button/menu |
| Pull to refresh | Reload | Refresh button |
| Pinch to zoom | Zoom | Zoom controls |
| Long press | Context menu | Menu button |

### Platform Gesture Differences

| Gesture | iOS | Android |
|---------|-----|---------|
| Back | Edge swipe left | System back |
| Dismiss modal | Swipe down | Back button/swipe |
| Context menu | Long press / Force touch | Long press |

---

## 5. Haptic Feedback Patterns

### iOS Haptic Types

| Type | Use Case |
|------|----------|
| `selection` | Picker, toggle |
| `light/medium/heavy` | Impact feedback |
| `success/warning/error` | Notification patterns |

### Usage Guidelines

```
✅ DO: Button taps, toggles, pull-to-refresh trigger, errors, confirmations
❌ DON'T: Every scroll, every list item, background events, too frequently
```

---

## 6. Mobile Cognitive Load

```
ONE PRIMARY ACTION per screen
PROGRESSIVE DISCLOSURE - show only what's needed
SMART DEFAULTS - pre-fill what you can
CHUNKING - break forms into steps
RECOGNITION over RECALL
```

### Miller's Law for Mobile

Desktop: 7±2 items. Mobile: 5±1 (more distractions). Max 5 tab bar items, 5 menu options.

---

## 7. Touch Accessibility

### Motor Impairment

- Generous targets (48dp+)
- Adjustable timing
- Undo for destructive actions
- Switch control support
- Voice control support

### WCAG 2.2

Touch targets: >= 44px width/height, >= 8px spacing from adjacent targets.

---

## 8. Touch Psychology Checklist

### Before Every Screen
- [ ] All touch targets >= 44-48px?
- [ ] Primary CTA in thumb zone?
- [ ] Destructive actions require confirmation?
- [ ] Gesture alternatives exist (visible buttons)?
- [ ] Haptic feedback on important actions?
- [ ] Immediate visual feedback on tap?
- [ ] Loading states for actions > 100ms?

### Before Release
- [ ] Tested on smallest supported device?
- [ ] Tested one-handed on large phone?
- [ ] Haptics work on device?
- [ ] No tiny close buttons or icons?

---

> **Remember:** Every touch is a conversation between user and device. Make it feel natural, responsive, and respectful of human fingers.
