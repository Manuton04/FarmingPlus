package fp.manuton.protection;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ProtectionManager.
 * Uses fake ProtectionCheck implementations to test the aggregation logic
 * without requiring WorldGuard, Towny, or a live server.
 */
public class ProtectionManagerTest {

    private ProtectionManager protectionManager;

    @BeforeEach
    void setUp() {
        protectionManager = new ProtectionManager();
    }

    // --- Fake ProtectionCheck for testing ---

    /**
     * A configurable fake protection check for unit testing.
     */
    private static class FakeProtectionCheck implements ProtectionCheck {
        private final String name;
        private final boolean enabled;
        private final boolean allowsBuild;

        FakeProtectionCheck(String name, boolean enabled, boolean allowsBuild) {
            this.name = name;
            this.enabled = enabled;
            this.allowsBuild = allowsBuild;
        }

        @Override
        public boolean canBuild(Player player, Location location) {
            return allowsBuild;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }

    // --- Tests ---

    @Test
    @DisplayName("No protection plugins registered → always allow")
    void testNoChecksRegistered() {
        assertTrue(protectionManager.canBuild(null, null),
                "Should allow building when no protection checks are registered");
    }

    @Test
    @DisplayName("Single protection allows → allow")
    void testSingleCheckAllows() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", true, true));

        assertTrue(protectionManager.canBuild(null, null),
                "Should allow when the only protection check allows");
    }

    @Test
    @DisplayName("Single protection denies → deny")
    void testSingleCheckDenies() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", true, false));

        assertFalse(protectionManager.canBuild(null, null),
                "Should deny when the only protection check denies");
    }

    @Test
    @DisplayName("Both WorldGuard and Towny allow → allow")
    void testBothAllow() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", true, true));
        protectionManager.register(new FakeProtectionCheck("Towny", true, true));

        assertTrue(protectionManager.canBuild(null, null),
                "Should allow when all protection checks allow");
    }

    @Test
    @DisplayName("WorldGuard allows, Towny denies → deny (AND logic)")
    void testWorldGuardAllowsTownyDenies() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", true, true));
        protectionManager.register(new FakeProtectionCheck("Towny", true, false));

        assertFalse(protectionManager.canBuild(null, null),
                "Should deny when ANY protection check denies (AND logic)");
    }

    @Test
    @DisplayName("WorldGuard denies, Towny allows → deny (AND logic)")
    void testWorldGuardDeniesTownyAllows() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", true, false));
        protectionManager.register(new FakeProtectionCheck("Towny", true, true));

        assertFalse(protectionManager.canBuild(null, null),
                "Should deny when ANY protection check denies (AND logic)");
    }

    @Test
    @DisplayName("Both deny → deny")
    void testBothDeny() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", true, false));
        protectionManager.register(new FakeProtectionCheck("Towny", true, false));

        assertFalse(protectionManager.canBuild(null, null),
                "Should deny when all protection checks deny");
    }

    @Test
    @DisplayName("Disabled protection is skipped → allow")
    void testDisabledCheckSkipped() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", false, false));

        assertTrue(protectionManager.canBuild(null, null),
                "Should skip disabled protection checks (not installed on server)");
    }

    @Test
    @DisplayName("WorldGuard disabled (denying), Towny enabled (allowing) → allow")
    void testDisabledWorldGuardEnabledTowny() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", false, false));
        protectionManager.register(new FakeProtectionCheck("Towny", true, true));

        assertTrue(protectionManager.canBuild(null, null),
                "Should only check enabled protections");
    }

    @Test
    @DisplayName("WorldGuard enabled (allowing), Towny disabled (denying) → allow")
    void testEnabledWorldGuardDisabledTowny() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", true, true));
        protectionManager.register(new FakeProtectionCheck("Towny", false, false));

        assertTrue(protectionManager.canBuild(null, null),
                "Should only check enabled protections");
    }

    @Test
    @DisplayName("Both disabled → allow (no protection active)")
    void testBothDisabled() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", false, false));
        protectionManager.register(new FakeProtectionCheck("Towny", false, false));

        assertTrue(protectionManager.canBuild(null, null),
                "Should allow when no protection plugins are active");
    }

    @Test
    @DisplayName("Three protections: two allow, one denies → deny")
    void testThreeProtectionsOneDenies() {
        protectionManager.register(new FakeProtectionCheck("WorldGuard", true, true));
        protectionManager.register(new FakeProtectionCheck("Towny", true, true));
        protectionManager.register(new FakeProtectionCheck("GriefPrevention", true, false));

        assertFalse(protectionManager.canBuild(null, null),
                "Should deny if any single protection denies, regardless of how many allow");
    }
}
