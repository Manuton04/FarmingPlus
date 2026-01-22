#!/bin/bash

# MySQL Connection Test Script for FarmingPlus
# This script tests if MySQL connection works independently

set -e

echo "=========================================="
echo "FarmingPlus - MySQL Connection Test"
echo "=========================================="

# Read MySQL configuration from config.yml
# Extract values using basic parsing
CONFIG_FILE="src/main/resources/config.yml"

if [ ! -f "$CONFIG_FILE" ]; then
    echo "❌ ERROR: config.yml not found at $CONFIG_FILE"
    exit 1
fi

# Check if MySQL is enabled
MYSQL_ENABLED=$(grep "enabled:" "$CONFIG_FILE" | grep "mysql" -A 1 | tail -n 1 | awk '{print $2}' | tr -d ',""')

if [ "$MYSQL_ENABLED" = "false" ] || [ "$MYSQL_ENABLED" = "" ]; then
    echo "⚠️  MySQL is disabled in config.yml"
    echo "   Set 'enabled: true' in config.mysql section to enable testing"
    exit 0
fi

echo "✓ MySQL is enabled in config"

# Extract MySQL config (basic parsing)
MYSQL_HOST=$(grep "host:" "$CONFIG_FILE" | grep "mysql" -A 3 | grep "host:" | awk '{print $2}' | tr -d ',""')
MYSQL_PORT=$(grep "port:" "$CONFIG_FILE" | grep "mysql" -A 3 | grep "port:" | awk '{print $2}')
MYSQL_DATABASE=$(grep "database:" "$CONFIG_FILE" | grep "mysql" -A 3 | grep "database:" | awk '{print $2}' | tr -d ',""')
MYSQL_USERNAME=$(grep "username:" "$CONFIG_FILE" | grep "mysql" -A 3 | grep "username:" | awk '{print $2}' | tr -d ',""')
MYSQL_PASSWORD=$(grep "password:" "$CONFIG_FILE" | grep "mysql" -A 3 | grep "password:" | awk '{print $2}' | tr -d ',""')

echo ""
echo "Configuration:"
echo "  Host: $MYSQL_HOST"
echo "  Port: $MYSQL_PORT"
echo "  Database: $MYSQL_DATABASE"
echo "  Username: $MYSQL_USERNAME"
echo ""

# Check if mysql client is available
if ! command -v mysql &> /dev/null; then
    echo "❌ ERROR: mysql client not found"
    echo "   Install mysql-client: apt-get install mysql-client (Debian/Ubuntu)"
    echo "                        or: yum install mysql (CentOS/RHEL)"
    echo ""
    echo "   Alternative: Compile and run the Java test instead"
    exit 1
fi

# Test 1: Connection Test
echo "Test 1: Testing MySQL connection..."
if mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USERNAME" -p"$MYSQL_PASSWORD" -e "SELECT 1;" &> /dev/null; then
    echo "✓ Successfully connected to MySQL server"
else
    echo "❌ Failed to connect to MySQL server"
    echo "   Check host, port, username, and password"
    exit 1
fi

# Test 2: Database Access Test
echo ""
echo "Test 2: Testing database access..."
if mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USERNAME" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" -e "SELECT 1;" &> /dev/null; then
    echo "✓ Successfully accessed database '$MYSQL_DATABASE'"
else
    echo "❌ Failed to access database '$MYSQL_DATABASE'"
    echo "   Check if database exists and user has permissions"
    exit 1
fi

# Test 3: Table Creation Test
echo ""
echo "Test 3: Testing table creation..."
CREATE_TABLE_QUERY="
CREATE TABLE IF NOT EXISTS farmingplus_rewards_table (
    uuid VARCHAR(36),
    Date TIMESTAMP,
    rewardName VARCHAR(255)
);
CREATE INDEX IF NOT EXISTS idx_uuid ON farmingplus_rewards_table (uuid);
"

if mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USERNAME" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" -e "$CREATE_TABLE_QUERY" &> /dev/null; then
    echo "✓ Successfully created/retrieved farmingplus_rewards_table"
else
    echo "❌ Failed to create/retrieve table"
    echo "   Check user permissions (CREATE, INDEX)"
    exit 1
fi

# Test 4: Insert Test
echo ""
echo "Test 4: Testing data insertion..."
TEST_UUID=$(uuidgen)
CURRENT_TIME=$(date +"%Y-%m-%d %H:%M:%S")

INSERT_QUERY="
INSERT INTO farmingplus_rewards_table (uuid, Date, rewardName)
VALUES ('$TEST_UUID', '$CURRENT_TIME', 'test_reward');
"

if mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USERNAME" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" -e "$INSERT_QUERY" &> /dev/null; then
    echo "✓ Successfully inserted test data"
else
    echo "❌ Failed to insert test data"
    echo "   Check user permissions (INSERT)"
    exit 1
fi

# Test 5: Query Test
echo ""
echo "Test 5: Testing data retrieval..."
SELECT_QUERY="
SELECT COUNT(*) as count FROM farmingplus_rewards_table WHERE uuid = '$TEST_UUID';
"

RESULT=$(mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USERNAME" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" -se "$SELECT_QUERY" 2>/dev/null)

if [ "$RESULT" -gt 0 ]; then
    echo "✓ Successfully queried test data (found $RESULT record(s))"
else
    echo "❌ Failed to query test data"
    exit 1
fi

# Test 6: Delete Test
echo ""
echo "Test 6: Testing data deletion..."
DELETE_QUERY="
DELETE FROM farmingplus_rewards_table WHERE uuid = '$TEST_UUID';
"

if mysql -h "$MYSQL_HOST" -P "$MYSQL_PORT" -u "$MYSQL_USERNAME" -p"$MYSQL_PASSWORD" "$MYSQL_DATABASE" -e "$DELETE_QUERY" &> /dev/null; then
    echo "✓ Successfully deleted test data"
else
    echo "❌ Failed to delete test data"
    echo "   Check user permissions (DELETE)"
    exit 1
fi

echo ""
echo "=========================================="
echo "✅ All MySQL tests passed!"
echo "=========================================="
echo ""
echo "Your MySQL connection is working correctly."
echo "The plugin should be able to:"
echo "  - Connect to the database"
echo "  - Create/retrieve tables"
echo "  - Insert, query, and delete data"
echo ""
