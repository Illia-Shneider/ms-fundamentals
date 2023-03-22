Feature: Resource processing

  Scenario: Success scenario
    When new resource available "41"
    Then success result

  Scenario: Fail scenario
    When new resource available "45"
    Then fail