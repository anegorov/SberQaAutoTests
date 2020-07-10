@translate
Feature: Authentication iam request validation

  Scenario: Check token
    Given set base url '{config{base}}'
    And resource is '/translate/v2/translate'
    And headers are
      | Content-Type  | application/json |
      | Authorization | Bearer NOT-VALID-TOCKEN |
    And create response body from template 'translate-request.json'
      |folder_id          | {config{folder_id}} |
      |texts              | Мир                 |
      |targetLanguageCode | en                  |
    When execute POST
    Then response code is 401
    And check response
      | code    | 16 |
      | message | The token is invalid |

