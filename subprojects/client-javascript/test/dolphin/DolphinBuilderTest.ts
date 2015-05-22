/// <reference path="../../testsuite/tsUnit.ts"/>
/// <reference path="../../js/dolphin/DolphinBuilder.ts"/>

module opendolphin {
    export class DolphinBuilderTest extends tsUnit.TestClass {

        default_values() {
            var builder = new DolphinBuilder();
            this.areIdentical(builder.url_, undefined, "ERROR: url_ must be 'undefined'");
            this.areIdentical(builder.reset_, false, "ERROR: reset_ must be 'false'");
            this.areIdentical(builder.slackMS_, 300, "ERROR: slackMS_ must be '300'");
        }

        url() {
            var url = 'http:8080//mydolphinapp';
            var builder = new DolphinBuilder().url(url);
            this.areIdentical(builder.url_, url, "ERROR: url_ must be '" + url + "'");
        }
        reset() {
            var reset = true;
            var builder = new DolphinBuilder().reset(reset);
            this.areIdentical(builder.reset_, reset, "ERROR: reset_ must be '" + reset + "'");
        }
        slackMS() {
            var slackMS = 500;
            var builder = new DolphinBuilder().slackMS(slackMS);
            this.areIdentical(builder.slackMS_, slackMS, "ERROR: slackMS_ must be '" + slackMS + "'");
        }

        built_clientDolphin() {
            var dolphin:ClientDolphin = new DolphinBuilder().build();
            this.areNotIdentical(dolphin.getClientConnector(), undefined, "ERROR: dolphin.clientConnector must be initialized");
            this.areNotIdentical(dolphin.getClientModelStore(), undefined, "ERROR: dolphin.clientModelStore must be initialized");
            // TODO: how to test if 'HttpTransmitter' or 'NoTransmitter' is created when 'ClientTransmitter.transmitter' is private ?
        }
    }

}