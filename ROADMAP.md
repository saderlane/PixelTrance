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

## Phase 1: Foundation (✅ Complete)
**Core Systems and Visuals**
- [ ] Mod loads and logs successfully
- [ ] Implement trance meter (player data + HUD)
  - [ ] Make it 🎀  𝓅𝓇𝑒𝓉𝓉𝓎  🎀
- [ ] Trance syncing in multiplayer
- [ ] Create base hypnosis item (Pocket Watch)
- [ ] Implement basic **Passive Focus Lock**
  - [ ] Add some conditional logic (gaze, etc)
  - [ ] Add screen pull to players
- [ ] Trance decay logic
- [ ] Focus decay logic
- [ ] Visual/audio feedback
  - [ ] Binaural plays when trance is at a certain percent
    - Scales with volume
  - [ ] Purple vignette when trance is at a certain percent
    - Pulses gently at even higher trance

---

## 🔓 Phase 2: Interactive Gameplay Loop *(Current Phase)*

**The goal of this phase is to establish a **functional gameplay loop**—players can hypnotize others, experience visible effects, and use tools meaningfully.**

### 🕰️ Pocket Watch Toggle Mode
- [ ] Right-click toggles “focus session” on/off
- [ ] Visual particles, subtle glow, looping ticking sound
  - Couldn't get visual particles to work and I don't think glow is a good effect
- [ ] Increases Passive Focus Lock buildup for viewers
- [ ] Screen pulls attention to source of Focus Lock
  - Can't get this to work for mobs - I'll likely have to build a full mob controller or something
- [ ] Only functions when targeting another entity (player or mob)
- [ ] Re-implement trance gain from pocket watch

### 🌀 Hypnotic Visual Feedback & Player Effects
Introduce progressively intense effects as the player’s trance level increases to enhance immersion and give players immediate sensory feedback.

These are mostly visual effects I will implement later as I want to move onto actual gameplay stuff

#### ✨ Low Trance (30–50%)
- [ ] Fade/blur edges of the screen
- [ ] Tooltip delay for "sluggish thought"

#### 🧠 High Trance (70%+)
- [ ] Player can’t jump or sprint

#### 💤 Full Trance (100%)
- [ ] Brightness fades in from the screen edges (vignette intensifies further)
- [ ] Display centered trance prompt
- [ ] Follow inducer
- [ ] Add `/suggest` command and make centered text appear

### 🔁 Escape Mechanic (Struggle System)
- [ ] Trance < 90
  - Shake mouse to lower focus and trance
- [ ] Trance = 90
  - Begin Pattern Mini-Game
  - Failing puts trance at 100
- [ ] Resistance stat applied on successful struggle
- [ ] Visual/audio feedback for escape attempts

### 🕯️ Candle & Pendulum Items (Passive Hypnosis Tools)
- [ ] Candle: Placeable aura item to build focus in radius
- [ ] Pendulum: Held item with pulsing AoE focus gain
- [ ] Effects apply to mobs and players in line of sight

### 🤝 Simple Mob Interaction
- [ ] Villagers pause and focus on hypnotist
- [ ] Animals slow down and follow player
- [ ] Whitelist/blacklist toggle for mob support

---

## 🌌 Planned Features

**Introduce light RPG elements to expand gameplay depth and replayability.**

### Skill Tree Framework
- [ ] Two main branches: Hypnotist / Subject
- [ ] Perks for focus gain, resistance, trance decay

### Resistance Stat System
- [ ] Passive resistance via traits or gear
- [ ] Slows trance gain or breaks locks faster

### Progression Framework
- [ ] XP through hypnosis/resistance
- [ ] Unlock perks from trance activity

---

### Immersion & Depth Effects

**Deepen the hypnotic experience with immersive visuals and behaviors.**

- [ ] Trance depth visuals (blur, fog, screen glow)
- [ ] Automatic behaviors (nodding, frozen stare)
- [ ] Emotes/cosmetics during hypnosis
- [ ] Visual posture changes under Focus Lock

---

### Mobs and More

**Begin to develop mobs that fit the hypnotic theme.**

- [ ] Manual triggers via commands/UI
- [ ] Custom emotes or actions during trance
- [ ] Configurable trance scene scripting
- [ ] Session tools for private/co-op RP
- [ ] Pose-based idle animations for trance

### Items and Blocks

- [ ] Clicker
- [ ] Bells

### RP & Customization Tools

**Optional, immersive features for RP players and content creators.**

- [ ] Manual triggers via commands/UI
- [ ] Custom emotes or actions during trance
- [ ] Configurable trance scene scripting
- [ ] Session tools for private/co-op RP
- [ ] Pose-based idle animations for trance

---

### Mod Configuration and Support

- [ ] Optional API for mod/plugin support
- [ ] Server config for trance/resistance rules

### Passive Development
- [ ] Create 3D Model for Pocket Watch item
  - [ ] Create swinging animation when item is in use
- [ ] Make the resistance to screen pull smoother

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
