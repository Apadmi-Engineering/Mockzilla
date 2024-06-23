// Autogenerated from Pigeon (v20.0.1), do not edit directly.
// See also: https://pub.dev/packages/pigeon
// ignore_for_file: public_member_api_docs, non_constant_identifier_names, avoid_as, unused_import, unnecessary_parenthesis, unnecessary_import, no_leading_underscores_for_local_identifiers
// ignore_for_file: avoid_relative_lib_imports
import 'dart:async';
import 'dart:typed_data' show Float64List, Int32List, Int64List, Uint8List;
import 'package:flutter/foundation.dart' show ReadBuffer, WriteBuffer;
import 'package:flutter/services.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:mockzilla_ios/src/messages.g.dart';


class _PigeonCodec extends StandardMessageCodec {
  const _PigeonCodec();
  @override
  void writeValue(WriteBuffer buffer, Object? value) {
    if (value is BridgeMockzillaHttpRequest) {
      buffer.putUint8(129);
      writeValue(buffer, value.encode());
    } else     if (value is BridgeMockzillaHttpResponse) {
      buffer.putUint8(130);
      writeValue(buffer, value.encode());
    } else     if (value is BridgeEndpointConfig) {
      buffer.putUint8(131);
      writeValue(buffer, value.encode());
    } else     if (value is BridgeReleaseModeConfig) {
      buffer.putUint8(132);
      writeValue(buffer, value.encode());
    } else     if (value is BridgeMockzillaConfig) {
      buffer.putUint8(133);
      writeValue(buffer, value.encode());
    } else     if (value is BridgeMockzillaRuntimeParams) {
      buffer.putUint8(134);
      writeValue(buffer, value.encode());
    } else     if (value is BridgeAuthHeader) {
      buffer.putUint8(135);
      writeValue(buffer, value.encode());
    } else     if (value is BridgeHttpMethod) {
      buffer.putUint8(136);
      writeValue(buffer, value.index);
    } else     if (value is BridgeLogLevel) {
      buffer.putUint8(137);
      writeValue(buffer, value.index);
    } else {
      super.writeValue(buffer, value);
    }
  }

  @override
  Object? readValueOfType(int type, ReadBuffer buffer) {
    switch (type) {
      case 129: 
        return BridgeMockzillaHttpRequest.decode(readValue(buffer)!);
      case 130: 
        return BridgeMockzillaHttpResponse.decode(readValue(buffer)!);
      case 131: 
        return BridgeEndpointConfig.decode(readValue(buffer)!);
      case 132: 
        return BridgeReleaseModeConfig.decode(readValue(buffer)!);
      case 133: 
        return BridgeMockzillaConfig.decode(readValue(buffer)!);
      case 134: 
        return BridgeMockzillaRuntimeParams.decode(readValue(buffer)!);
      case 135: 
        return BridgeAuthHeader.decode(readValue(buffer)!);
      case 136: 
        final int? value = readValue(buffer) as int?;
        return value == null ? null : BridgeHttpMethod.values[value];
      case 137: 
        final int? value = readValue(buffer) as int?;
        return value == null ? null : BridgeLogLevel.values[value];
      default:
        return super.readValueOfType(type, buffer);
    }
  }
}
