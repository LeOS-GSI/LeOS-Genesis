/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package org.leos.telemetry.ping;

import org.json.JSONObject;
import org.leos.telemetry.config.TelemetryConfiguration;
import org.leos.telemetry.measurement.ArchMeasurement;
import org.leos.telemetry.measurement.CreatedDateMeasurementNew;
import org.leos.telemetry.measurement.CreatedTimestampMeasurementNew;
import org.leos.telemetry.measurement.DeviceMeasurement;
import org.leos.telemetry.measurement.FirstRunProfileDateMeasurement;
import org.leos.telemetry.measurement.LocaleMeasurement;
import org.leos.telemetry.measurement.MetricsMeasurement;
import org.leos.telemetry.measurement.OperatingSystemMeasurement;
import org.leos.telemetry.measurement.OperatingSystemVersionMeasurement;
import org.leos.telemetry.measurement.ProcessStartTimestampMeasurement;
import org.leos.telemetry.measurement.SequenceMeasurement;
import org.leos.telemetry.measurement.TimezoneOffsetMeasurement;

/**
 * A telemetry ping builder for events of type "mobile-metrics".
 *
 * See the schema for more details:
 *   https://github.com/mozilla-services/mozilla-pipeline-schemas/blob/master/schemas/telemetry/mobile-metrics/mobile-metrics.1.schema.json
 */
public class TelemetryMobileMetricsPingBuilder extends TelemetryPingBuilder {
    public static final String TYPE = "mobile-metrics";
    private static final int VERSION = 1;

    public TelemetryMobileMetricsPingBuilder(JSONObject snapshots, TelemetryConfiguration configuration) {
        super(configuration, TYPE, VERSION);

        addMeasurement(new ProcessStartTimestampMeasurement(configuration));
        addMeasurement(new SequenceMeasurement(configuration, this));
        addMeasurement(new LocaleMeasurement());
        addMeasurement(new DeviceMeasurement());
        addMeasurement(new ArchMeasurement());
        addMeasurement(new FirstRunProfileDateMeasurement(configuration));
        addMeasurement(new OperatingSystemMeasurement());
        addMeasurement(new OperatingSystemVersionMeasurement());
        addMeasurement(new CreatedDateMeasurementNew());
        addMeasurement(new CreatedTimestampMeasurementNew());
        addMeasurement(new TimezoneOffsetMeasurement());
        addMeasurement(new MetricsMeasurement(snapshots));
    }

    @Override
    protected String getUploadPath(final String documentId) {
        return super.getUploadPath(documentId) + "?v=4";
    }
}
