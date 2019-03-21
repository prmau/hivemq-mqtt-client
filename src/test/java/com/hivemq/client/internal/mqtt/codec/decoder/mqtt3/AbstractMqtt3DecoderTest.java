/*
 * Copyright 2018 dc-square and the HiveMQ MQTT Client Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hivemq.client.internal.mqtt.codec.decoder.mqtt3;

import com.hivemq.client.internal.mqtt.codec.decoder.AbstractMqttDecoderTest;
import com.hivemq.client.internal.mqtt.codec.decoder.MqttMessageDecoders;
import com.hivemq.client.internal.mqtt.message.connect.mqtt3.Mqtt3ConnectView;
import com.hivemq.client.mqtt.MqttVersion;
import org.jetbrains.annotations.NotNull;

/**
 * @author Silvio Giebl
 */
abstract class AbstractMqtt3DecoderTest extends AbstractMqttDecoderTest {

    AbstractMqtt3DecoderTest(final @NotNull MqttMessageDecoders decoders) {
        super(decoders, MqttVersion.MQTT_3_1_1, Mqtt3ConnectView.DEFAULT.getDelegate());
    }
}