/*
 * This file is generated by jOOQ.
 */
package DB.Tables.tables;


import DB.Tables.FootballsystemDb;
import DB.Tables.Keys;
import DB.Tables.tables.records.FanNotificationRecord;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row6;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class FanNotification extends TableImpl<FanNotificationRecord> {

    private static final long serialVersionUID = 282166282;

    /**
     * The reference instance of <code>footballsystem_db.fan_notification</code>
     */
    public static final FanNotification FAN_NOTIFICATION = new FanNotification();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<FanNotificationRecord> getRecordType() {
        return FanNotificationRecord.class;
    }

    /**
     * The column <code>footballsystem_db.fan_notification.match_date</code>.
     */
    public final TableField<FanNotificationRecord, LocalDateTime> MATCH_DATE = createField(DSL.name("match_date"), org.jooq.impl.SQLDataType.LOCALDATETIME.nullable(false), this, "");

    /**
     * The column <code>footballsystem_db.fan_notification.home_team</code>.
     */
    public final TableField<FanNotificationRecord, String> HOME_TEAM = createField(DSL.name("home_team"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>footballsystem_db.fan_notification.away_team</code>.
     */
    public final TableField<FanNotificationRecord, String> AWAY_TEAM = createField(DSL.name("away_team"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>footballsystem_db.fan_notification.userName</code>.
     */
    public final TableField<FanNotificationRecord, String> USERNAME = createField(DSL.name("userName"), org.jooq.impl.SQLDataType.VARCHAR(50).nullable(false), this, "");

    /**
     * The column <code>footballsystem_db.fan_notification.event_id</code>.
     */
    public final TableField<FanNotificationRecord, Integer> EVENT_ID = createField(DSL.name("event_id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.INTEGER)), this, "");

    /**
     * The column <code>footballsystem_db.fan_notification.readed</code>.
     */
    public final TableField<FanNotificationRecord, Byte> READED = createField(DSL.name("readed"), org.jooq.impl.SQLDataType.TINYINT.nullable(false).defaultValue(org.jooq.impl.DSL.field("0", org.jooq.impl.SQLDataType.TINYINT)), this, "");

    /**
     * Create a <code>footballsystem_db.fan_notification</code> table reference
     */
    public FanNotification() {
        this(DSL.name("fan_notification"), null);
    }

    /**
     * Create an aliased <code>footballsystem_db.fan_notification</code> table reference
     */
    public FanNotification(String alias) {
        this(DSL.name(alias), FAN_NOTIFICATION);
    }

    /**
     * Create an aliased <code>footballsystem_db.fan_notification</code> table reference
     */
    public FanNotification(Name alias) {
        this(alias, FAN_NOTIFICATION);
    }

    private FanNotification(Name alias, Table<FanNotificationRecord> aliased) {
        this(alias, aliased, null);
    }

    private FanNotification(Name alias, Table<FanNotificationRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> FanNotification(Table<O> child, ForeignKey<O, FanNotificationRecord> key) {
        super(child, key, FAN_NOTIFICATION);
    }

    @Override
    public Schema getSchema() {
        return FootballsystemDb.FOOTBALLSYSTEM_DB;
    }

    @Override
    public UniqueKey<FanNotificationRecord> getPrimaryKey() {
        return Keys.KEY_FAN_NOTIFICATION_PRIMARY;
    }

    @Override
    public List<UniqueKey<FanNotificationRecord>> getKeys() {
        return Arrays.<UniqueKey<FanNotificationRecord>>asList(Keys.KEY_FAN_NOTIFICATION_PRIMARY);
    }

    @Override
    public List<ForeignKey<FanNotificationRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<FanNotificationRecord, ?>>asList(Keys.FK_FAN_NOTIFICATION_FAN_MATCHES_FOLLOW_2, Keys.FK_FAN_NOTIFICATION_EVENTS);
    }

    public FanMatchesFollow fanMatchesFollow() {
        return new FanMatchesFollow(this, Keys.FK_FAN_NOTIFICATION_FAN_MATCHES_FOLLOW_2);
    }

    public Events events() {
        return new Events(this, Keys.FK_FAN_NOTIFICATION_EVENTS);
    }

    @Override
    public FanNotification as(String alias) {
        return new FanNotification(DSL.name(alias), this);
    }

    @Override
    public FanNotification as(Name alias) {
        return new FanNotification(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public FanNotification rename(String name) {
        return new FanNotification(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public FanNotification rename(Name name) {
        return new FanNotification(name, null);
    }

    // -------------------------------------------------------------------------
    // Row6 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row6<LocalDateTime, String, String, String, Integer, Byte> fieldsRow() {
        return (Row6) super.fieldsRow();
    }
}