# Worth My Time

An Android app that converts any item's price into "life hours" (hours, workdays, workweeks of labor after tax), helping you make more informed purchasing decisions.

## Features

- **Price to Time Conversion**: Convert any price into hours, workdays, or workweeks of labor
- **Tax Support**: Includes optional sales tax (manual or by US state)
- **Lifestyle Equivalents**: See prices in terms of coffees, days of groceries, streaming months
- **Goals Tracking**: Save items as goals with progress tracking
- **History**: Keep track of recent price checks with worth/not worth decisions
- **Offline-First**: No network required, all data stored locally
- **Monthly Insights**: Track spending patterns and get tips

## Architecture


- **Platform**: Android (Kotlin, Jetpack Compose, Material 3)
- **Architecture**: MVVM with Kotlin Flow/StateFlow
- **Dependency Injection**: Hilt
- **Persistence**: Room (Goals, History), DataStore (Profile + Settings)
- **Navigation**: androidx.navigation.compose with bottom navigation
- **Performance**: Optimized for smooth 60fps scrolling and < 100ms calculations

## Project Structure

```
com.worthmytime/
├── data/
│   ├── datastore/          # Profile/Settings sources
│   ├── db/                 # Room database, DAOs, Entities
│   ├── repo/               # Repositories
│   └── assets/             # sales_tax_states.json
├── domain/
│   ├── model/              # Domain models, Enums
│   └── logic/              # Calculation functions
├── ui/
│   ├── navigation/         # Navigation setup
│   ├── home/              # Home screen
│   ├── goals/             # Goals screen
│   ├── insights/          # Insights screen
│   ├── settings/          # Settings screen
│   ├── components/        # Shared composables
│   └── theme/             # Material 3 theme
└── di/                    # Hilt modules
```

## Key Components

### Domain Logic
- `Calculator.kt`: Core calculation functions for price-to-time conversion
- `Profile.kt`: User profile and settings data model
- Various enums for type safety

### Data Layer
- `ProfileRepository`: Manages user profile and settings via DataStore
- `GoalsRepository`: Manages goals data via Room
- `HistoryRepository`: Manages history data via Room
- `TaxRepository`: Manages sales tax data from bundled JSON

### UI Layer
- `HomeScreen`: Main calculator with price input and results
- `GoalsScreen`: Goal tracking and progress
- `InsightsScreen`: Monthly insights and analytics
- `SettingsScreen`: User preferences and configuration

## Setup

1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Build and run on device or emulator

## Dependencies

- **Jetpack Compose BOM**: 2023.10.01
- **Material 3**: Latest version
- **Room**: 2.6.1 with KSP
- **Hilt**: 2.48 for dependency injection
- **DataStore**: 1.0.0 for preferences
- **Navigation Compose**: 2.7.5

## Performance Targets

- Cold start to interactive Home < 2s on mid-range device
- Price input to updated result < 100ms
- Smooth 60fps scrolling for lists
- All data reads are Flow-backed and lightweight

## Data Privacy

- All data stored locally on device
- No network requests required
- Optional data export functionality
- Privacy controls for hiding dollar amounts in history

## Future Enhancements

- Goal forecasting with weekly contribution tracking
- Advanced insights with category breakdowns
- Export data to CSV/JSON
- Widget support for quick calculations
- Dark mode and theme customization

## License

This project is licensed under the MIT License - see the LICENSE file for details.
