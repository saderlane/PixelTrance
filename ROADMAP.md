# 🌀 PixelTrance Mod Roadmap

*A Minecraft mod that pays homage to the art of hypnosis, blending RPG progression with immersive trance mechanics for both singleplayer and multiplayer.*

---

## 🎯 Core Goals

- Hypnosis as a central mechanic, not a gimmick
- Support for both **singleplayer** and **multiplayer**
- Players can be **hypnotists**, **subjects**, or both
- RPG-style **skill/perk system**
- **Trance meter** represents susceptibility and control
- Items, abilities, and mobs interact with trance mechanics

---

# 📅 Roadmap Phases

## 🌱 Phase 1: Foundation (✅ Complete)
**Core Systems and Visuals**
- [x] Mod loads and logs successfully
- [X] Implement trance meter (player data + HUD)
  - [X] Make it 🎀  𝓅𝓇𝑒𝓉𝓉𝓎  🎀
- [X] Trance syncing in multiplayer
- [X] Create base hypnosis item (Pocket Watch)
- [X] Implement basic **Passive Focus Lock**
- [X] Trance gain/decay logic
- [X] Visual/audio feedback
  - [X] Binaural plays when trance is at a certain percent
    - Scales with volume
  - [X] Purple vignette when trance is at a certain percent
    - Pulses gently at even higher trance

---

## 🔓 Phase 2: Interactive Gameplay Loop *(Current Phase)*

**The goal of this phase is to establish a **functional gameplay loop**—players can hypnotize others, experience visible effects, and use tools meaningfully.**

### 🕰️ Pocket Watch Toggle Mode
- [X] Right-click toggles “focus session” on/off
- [X] ~~Visual particles~~, ~~subtle glow~~, looping ticking sound
  - Couldn't get visual particles to work and I don't think glow is a good effect
- [X] Increases Passive Focus Lock buildup for viewers
- [X] Screen pulls attention to source of Focus Lock
  - Can't get this to work for mobs - I'll likely have to build a full mob controller or something
- [X] Only functions when targeting another entity (player or mob)
- [X] Re-implement trance gain from pocket watch

### 🌀 Hypnosis Effect Trigger
- [ ] Trigger hypnosis event when trance threshold is reached
- [ ] Enter “hypnotized” state (immobile or slowed)
- [ ] Visual swirl or blur overlay
- [ ] Temporary control reduction

### 🔁 Escape Mechanic (Struggle System)
- [ ] Shake mouse or press keys to resist hypnosis
- [ ] Resistance stat applied on successful struggle
- [ ] Visual/audio feedback for escape attempts

### 🕯️ Candle & Pendulum Items (Passive Hypnosis Tools)
- [ ] Candle: Placeable aura item to build focus in radius
- [ ] Pendulum: Held item with pulsing AoE focus gain
- [ ] Effects apply to mobs and players in line of sight

### 🤝 Simple Mob Interaction
- [ ] Villagers pause and focus on hypnotist
- [ ] Animals slow down or become dazed
- [ ] Whitelist/blacklist toggle for mob support

---

## 🌳 Phase 3: Skill Tree & Resistance

**Introduce light RPG elements to expand gameplay depth and replayability.**

### 🧠 Skill Tree Framework
- [ ] Two main branches: Hypnotist / Subject
- [ ] Perks for focus gain, resistance, trance decay

### 🛡️ Resistance Stat System
- [ ] Passive resistance via traits or gear
- [ ] Slows trance gain or breaks locks faster

### 📜 Progression Framework
- [ ] XP through hypnosis/resistance
- [ ] Unlock perks from trance activity

---

## 🌌 Phase 4: Immersion & Depth Effects

**Deepen the hypnotic experience with immersive visuals and behaviors.**

- [ ] Trance depth visuals (blur, fog, screen glow)
- [ ] Automatic behaviors (nodding, frozen stare)
- [ ] Emotes/cosmetics during hypnosis
- [ ] Visual posture changes under Focus Lock

---

## 🎭 Phase 5: RP & Customization Tools

**Optional, immersive features for RP players and content creators.**

- [ ] Manual triggers via commands/UI
- [ ] Custom emotes or actions during trance
- [ ] Configurable trance scene scripting
- [ ] Session tools for private/co-op RP
- [ ] Pose-based idle animations for trance

---

## 🧩 Phase 6: Advanced Multiplayer & Mod Support

- [ ] Server-side syncing of trance and focus
- [ ] Optional API for mod/plugin support
- [ ] Server config for trance/resistance rules

### 🎨 Phase X: Passive Development
- [ ] Create 3D Model for Pocket Watch item
  - [ ] Create swinging animation when item is in use

---

## 🪄 Trance Induction System (Hybrid Design)

### Default: Passive Focus Lock
- Use item (right-click) to lock focus
- Builds trance over time while eye contact held
- Cancels on distraction or movement

### Optional: Mind Duel Mode
- Activated via keybind or RP agreement
- Mini-game (rhythm, reaction-based)
- Skill, gear, and perks affect outcome

### Pulse-Based Items
- Instant trance pulses (pendulum, etc.)
- Quick-use, lower trance gain
- Great for action/combat scenarios

### Item Types Overview

| Item        | Mechanic                     | Notes                            |
|-------------|------------------------------|----------------------------------|
| Pocket Watch | Focused gaze + build-up     | Strong, requires steady aim      |
| Pendulum     | AoE pulses on right-click   | Easy to use, lower effect        |
| Candle       | Passive aura                | Trance over time in radius       |
| Trigger Token| Delayed suggestion effect   | Anchors for advanced control     |

---

*This file is part of the PixelTrance mod planning. For live progress, see the GitHub Project Board.*