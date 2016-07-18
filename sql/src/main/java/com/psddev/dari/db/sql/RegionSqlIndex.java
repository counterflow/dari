package com.psddev.dari.db.sql;

import com.psddev.dari.db.ObjectIndex;
import com.psddev.dari.db.Region;
import com.psddev.dari.util.CompactMap;
import org.jooq.Param;
import org.jooq.impl.DSL;

import java.util.Map;

class RegionSqlIndex extends AbstractSqlIndex {

    private final Param<String> regionParam;
    private final Object valueParam;

    public RegionSqlIndex(SqlSchema schema, String namePrefix, int version) {
        super(schema, namePrefix, version);

        this.regionParam = DSL.param("region", String.class);
        this.valueParam = schema.stGeomFromText(regionParam);
    }

    @Override
    public Object valueParam() {
        return valueParam;
    }

    @Override
    public Map<String, Object> valueBindValues(ObjectIndex index, Object value) {
        if (value instanceof Region) {
            Map<String, Object> bindValues = new CompactMap<>();
            bindValues.put(regionParam.getName(), ((Region) value).toMultiPolygonWkt());
            return bindValues;

        } else {
            return null;
        }
    }

    @Override
    public Param<?> valueInline(ObjectIndex index, Object value) {
        return value instanceof Region
                ? DSL.inline(schema.stGeomFromText(DSL.inline(((Region) value).toMultiPolygonWkt(), String.class)))
                : null;
    }
}